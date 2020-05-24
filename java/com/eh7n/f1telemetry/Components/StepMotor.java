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
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
/**
 * This example code demonstrates how to perform simple state
 * control of a GPIO pin on the Raspberry Pi.
 *
 * @author Robert Savage
 */
public class StepMotor {
    public final int stepcount = 8;
    public int newSpeed;
    public int oldSpeed;
    public int steps;
    public int position;
    public final int[][] seq ;
    public final boolean  button ;
    public final GpioPinDigitalOutput coil_A_2_pin ;
    public final GpioPinDigitalOutput coil_B_2_pin ;
    public final GpioPinDigitalOutput coil_A_1_pin ;
    public final GpioPinDigitalOutput coil_B_1_pin;
    public final GpioPinDigitalInput myButton;
    //private  int flag=1;
    public StepMotor()throws InterruptedException{
        //this.newSpeed = newSpeed;
        //oldSpeed = newSpeed;
        //newSpeed = nSpeed;
        System.out.println("<--Pi4J--> GPIO Control Example ... started.");
        // create gpio controller
        //gpio = GpioFactory.getInstance();
        // provision gpio pin #01 as an output pin and turn on
        //pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "MyLED", PinState.HIGH);
        //stepcount = 8;
        coil_A_2_pin = F12018TelemetryUDPServer.getCoil_A_2_pin();
        coil_B_2_pin = F12018TelemetryUDPServer.getCoil_B_2_pin();
        coil_A_1_pin = F12018TelemetryUDPServer.getCoil_A_1_pin();
        coil_B_1_pin = F12018TelemetryUDPServer.getCoil_B_1_pin();
        myButton = F12018TelemetryUDPServer.getMyButton();
        seq = new int[stepcount][];
        button = false;
        seq[0]= new int[]{1,0,0,0};
        seq[1] = new int[]{1,1,0,0};
        seq[2] = new int[]{0,1,0,0};
        seq[3] = new int[]{0,1,1,0};
        seq[4] = new int[]{0,0,1,0};
        seq[5] = new int[]{0,0,1,1};
        seq[6] = new int[]{0,0,0,1};
        seq[7] = new int[]{1,0,0,1};

        initialise();
        //steps=80;
        Thread.sleep(1000);

    }

    public void initialise() throws InterruptedException
    {   position = 0;
        System.out.println("--> motor forwards 512 steps");
        //pin.high(); // set second argument to 'true' use a blocking call
        for(int i=0;i<512;i++)
        {
            myButton.addListener(new GpioPinListenerDigital()
            {
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event)
                {
                    // display pin state on console
                    System.out.println(" Switch change detected: " + event.getPin() + " = " + event.getState());

                }

            });
            if(myButton.isHigh())
            {
                System.out.println("aaaaaaaa");
                break;
            }
            for(int j = stepcount -1 ;j>=0;j--)
            {
                //setStep(Seq[j][0], Seq[j][1], Seq[j][2], Seq[j][3])
                if(seq[j][0]==1)
                {
                    coil_A_1_pin.high();
                }
                else
                {
                    coil_A_1_pin.low();
                }
                if(seq[j][1]==1)
                {
                    coil_A_2_pin.high();
                }
                else
                {
                    coil_A_2_pin.low();
                }
                if(seq[j][2]==1)
                {
                    coil_B_1_pin.high();
                }
                else
                {
                    coil_B_1_pin.low();
                }
                if(seq[j][3]==1)
                {
                    coil_B_2_pin.high();
                }
                else
                {
                    coil_B_2_pin.low();
                }
                //TimeUnit.MICROSECONDS.sleep(1);
                Gpio.delayMicroseconds(700);

            }
        }
    }
    public void moveCW() throws InterruptedException
    {
      //  System.out.println("--> motor CW " + steps);
       // pin.high(); // set second argument to 'true' use a blocking call
        for(int i=0;i<steps;i++)
        {
            position++;
            for(int j = 0;j<stepcount;j++)
            {
                //setStep(Seq[j][0], Seq[j][1], Seq[j][2], Seq[j][3])
                if(seq[j][0]==1)
                {
                    coil_A_1_pin.high();
                }
                else
                {
                    coil_A_1_pin.low();
                }
                if(seq[j][1]==1)
                {
                    coil_A_2_pin.high();
                }
                else
                {
                    coil_A_2_pin.low();
                }
                if(seq[j][2]==1)
                {
                    coil_B_1_pin.high();
                }
                else
                {
                    coil_B_1_pin.low();
                }
                if(seq[j][3]==1)
                {
                    coil_B_2_pin.high();
                }
                else
                {
                    coil_B_2_pin.low();
                }
                Thread.sleep(1);

            }
        }
    }
    public void moveCCW() throws InterruptedException
    {
        //System.out.println("--> motor CCW" + steps);
       // pin.high(); // set second argument to 'true' use a blocking call
        for(int i=0;i<steps;i++)
        {
            position--;
            for(int j = stepcount -1 ;j>=0;j--)
            {
                //setStep(Seq[j][0], Seq[j][1], Seq[j][2], Seq[j][3])
                if(seq[j][0]==1)
                {
                    coil_A_1_pin.high();
                }
                else
                {
                    coil_A_1_pin.low();
                }
                if(seq[j][1]==1)
                {
                    coil_A_2_pin.high();
                }
                else
                {
                    coil_A_2_pin.low();
                }
                if(seq[j][2]==1)
                {
                    coil_B_1_pin.high();
                }
                else
                {
                    coil_B_1_pin.low();
                }
                if(seq[j][3]==1)
                {
                    coil_B_2_pin.high();
                }
                else
                {
                    coil_B_2_pin.low();
                }
                Thread.sleep(1);

            }
        }
    }
    public void setSpeed(int nSpeed) throws InterruptedException
    {
        oldSpeed = newSpeed;
        newSpeed = nSpeed;
        steps = newSpeed;
        if((newSpeed>oldSpeed))
        {
            if(steps - position<= 350)
            {
                steps = steps-position;
            }
            else
            {
                steps = 350-position;
            }
            moveCW();
        }
        else
        {
            if(position - steps >= 0)
            {
                steps = position - steps;
            }
            else
            {
                steps = position;
            }
            moveCCW();
        }
    }
}


