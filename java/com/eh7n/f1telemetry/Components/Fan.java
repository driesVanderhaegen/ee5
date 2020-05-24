package com.eh7n.f1telemetry.Components;
import java.util.concurrent.TimeUnit;
import com.eh7n.f1telemetry.F12018TelemetryUDPServer;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import static java.lang.Thread.sleep;
import com.pi4j.io.gpio.*;
import com.pi4j.util.CommandArgumentParser;


import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
/**
 * This example code demonstrates how to perform simple state
 * control of a GPIO pin on the Raspberry Pi.
 *
 * @author Robert Savage
 */
public class Fan {
    public final int stepcount = 8;
    public int newSpeed;
    public int oldSpeed;
    public int steps;
    public int duty;

    public final GpioPinPwmOutput pwm;
    //private  int flag=1;
    public Fan()throws InterruptedException{
        //this.newSpeed = newSpeed;
        //oldSpeed = newSpeed;
        //newSpeed = nSpeed;
        System.out.println("<--Pi4J--> GPIO Control Example ... started.");
        pwm = F12018TelemetryUDPServer.getPwm();

        /*Pin pin1 = CommandArgumentParser.getPin(
                RaspiPin.class,    // pin provider class to obtain pin instance from
                RaspiPin.GPIO_01);             // argument array to search in

        GpioPinPwmOutput pwm = gpio.provisionPwmOutputPin(pin1);*/

        // you can optionally use these wiringPi methods to further customize the PWM generator
        // see: http://wiringpi.com/reference/raspberry-pi-specifics/
       // com.pi4j.wiringpi.Gpio.pwmSetMode(com.pi4j.wiringpi.Gpio.PWM_MODE_MS);
        com.pi4j.wiringpi.Gpio.pwmSetRange(100);
        com.pi4j.wiringpi.Gpio.pwmSetClock(4);
        System.out.println("--> PWM fann : 100");

        // set the PWM rate to 500
        pwm.setPwm(100);

        Thread.sleep(1000);
    }



    public void setDuty(int dutyn) throws InterruptedException
    {
         duty = map(dutyn,0,300,0,100);
         if(duty>40){duty = 100;}
        pwm.setPwm(duty);
        System.out.println("--> PWM fan: " +duty);
    }
    int map(int x, int in_min, int in_max, int out_min, int out_max)
    {
        return  ((x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
    }
}


