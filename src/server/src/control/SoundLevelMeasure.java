package control;

import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class SoundLevelMeasure
{
	private boolean running;
	private int volume = 0;
	private float averageVolume = 0;
	private float maxVolume1 = 400;
	private AudioManager audioManager;
	
	private static AudioFormat getFormat()
	{
		float sampleRate = 8000;
		int sampleSizeInBits = 8;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = true;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}
	
	public SoundLevelMeasure(AudioManager audioManager)
	{
		this.audioManager = audioManager;
		
		try
		{
			final AudioFormat format = getFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();
			Runnable runner = new Runnable()
			{
				int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
				byte buffer[] = new byte[bufferSize];
				
				public void run()
				{
					running = true;
					short samples = 0;
					ArrayList<Float> volumes = new ArrayList<Float>();
					while (running)
					{
						int count = line.read(buffer, 0, buffer.length);
						if (count > 0)
						{
							float rms = RootMeanSquared(buffer);
							rms *= 100;
							volume = (int) rms;
							// System.out.println("RMS: " + rms);
							
							if (samples < 10)
							{
								volumes.add(rms);
								samples++;
							}
							else
							{
								float volsum = 0;
								for (float vol : volumes)
								{
									volsum += vol;
								}
								averageVolume = volsum / volumes.size();
								
								adjustVolume();
								
								System.out.println("Average RMS volume: " + averageVolume);
								
								samples = 0;
								volumes.clear();
							}
						}
					}
				}
			};
			Thread captureThread = new Thread(runner);
			captureThread.start();
		}
		catch (LineUnavailableException e)
		{
			System.err.println("Line unavailable: " + e);
			System.exit(-2);
		}
	}
	
	private void adjustVolume()
	{
		float volumeDiff = (averageVolume - maxVolume1);
		float volumeDiv = (maxVolume1 / averageVolume);
		if (averageVolume > maxVolume1)
		{
			audioManager.setVolume((volumeDiv * 0.85f) * audioManager.getVolume());
		}
		else if (volumeDiv > 1.2)
		{
			audioManager.setVolume((volumeDiv * 1.15f) * audioManager.getVolume());
		}
		System.out.println("Volume: " + audioManager.getVolume() * 100 + "%");
	}
	
	public static float RootMeanSquared(byte[] audio)
	{
		double sumOfSquared = 0;
		for (int i = 0; i < audio.length; i++)
		{
			sumOfSquared += audio[i] * audio[i];
		}
		return (float) Math.sqrt(sumOfSquared / (double) audio.length);
	}
	
	public int getVolume()
	{
		return volume;
	}
}
