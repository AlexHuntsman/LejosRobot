package bot.model;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class EV3Bot 
{
	private String botMessage;
	private int xPosition;
	private int yPosition;
	private long waitTime;
	
	private MovePilot botPilot;
	
	private EV3UltrasonicSensor distanceSensor;
	private EV3TouchSensor backTouch;
	private double [] roomDistance;
	private float [] ultrasonicSamples;
	private float [] touchSamples;
	
	public EV3Bot()
	{
		this.botMessage = "Alex's Bot";
		this.xPosition = 50;
		this.yPosition = 50;
		this.waitTime = 4000;
		
		distanceSensor = new EV3UltrasonicSensor(LocalEV3.get().getPort("S1"));
		backTouch = new EV3TouchSensor(LocalEV3.get().getPort("S2"));
		
		roomDistance = new double[4];
		
		setupDistanceArray();
		setupPilot();
		
		displayMessage();
	}
	
	private void setupDistanceArray()
	{
		roomDistance[0] = (12 * 2.54 * 3);
		roomDistance[1] = (12 * 2.54 * 10.5);
		roomDistance[2] = (12 * 2.54 * 17);
		roomDistance[3] = (12 * 2.54 * 19);
	}
	/**
	 * used to Initilize a MovePilot object outside of the constructor.
	 */
	
	private void setupPilot()
	{
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 55.0).offset(-72);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.B, 55.0).offset(-72);
		WheeledChassis chassis = new WheeledChassis(new Wheel[]{leftWheel, rightWheel}, WheeledChassis);
		botPilot = new MovePilot(chassis);
	}
	
	public void driveRoom()
	{
		ultrasonicSamples = new float [distanceSensor.sampleSize()];
		distanceSensor.fetchSample(ultrasonicSample, 0);
		if(ultrasonicSamples[0] < 2.5)//2.5 is not a real number! figure out a better number
		{
			displayMessage("Short Drive");
			driveShort();
		}
		else
		{
			displayMessage("Long Drive");
			driveLong();
		}
		displayMessage("I a, at the other door!!!");
	}
	
	//overload the displayMessage method with a single String parameter
	private void displayMessage(String message)
	{
		LCD.drawString(botMessage, xPosition, yPosition);
		Delay.msDelay(waitTime);
	}
	//write your first drive method here
	//call the displayMessage("describe action as a string") in the helper method
	
	
	
	
	public void driveAround()
	{
		while(LocalEV3.get().getKeys().waitForAnyPress() != LocalEV3.get().getKeys().ID_ESCAPE)
		{
			double distance = (Math.random() * 100) % 23;
			double angle = (Math.random() * 360);
			boolean isPositive = ((int) (Math.random() * 2) % 2 == 0);
			distanceSensor.fetchSample(ultrasonicSamples, 0);
			if(ultrasonicSamples[0] < 17)
			{
				botPilot.travel(-distance);
				botPilot.rotate(angle);
			}
			else
			{
				botPilot.rotate(-angle);
				botPilot.travel(distance);
			}
		}
	}
}
