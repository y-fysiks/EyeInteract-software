package com.eyetrackerfrontend.eyetrackerfrontend;

import java.util.Locale;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

public class TTS {
    private Synthesizer synthesizer;
    public TTS() {
        try {
            System.setProperty(
                    "freetts.voices",
                    "com.sun.speech.freetts.en.us"
                            + ".cmu_us_kal.KevinVoiceDirectory");

            // Register Engine
            Central.registerEngineCentral(
                    "com.sun.speech.freetts"
                            + ".jsapi.FreeTTSEngineCentral");

            // Create a Synthesizer
            synthesizer = Central.createSynthesizer(
                    new SynthesizerModeDesc(Locale.US));

            // Allocate synthesizer
            synthesizer.allocate();

            // Resume Synthesizer
            synthesizer.resume();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void speak(String s) {
        try {
            // Speaks the given text
            synthesizer.speakPlainText(s, null);

            // until the queue is empty.
            //synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
        } catch (Exception e) {
            System.out.println("An error occurred while attempting to speak");
            e.printStackTrace();
        }
    }

    public void deallocate() {
        // Deallocate the Synthesizer.
        try {
            synthesizer.deallocate();
            System.out.println("synthesizer deallocated");
        } catch (EngineException e) {
            System.out.println("An error occurred while attempting to shut down the voice synthesizer");
        }
    }

    public static void main(String[] args)
    {

        try {
            // Set property as Kevin Dictionary
            System.setProperty(
                    "freetts.voices",
                    "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

            // Register Engine
            Central.registerEngineCentral(
                    "com.sun.speech.freetts"
                            + ".jsapi.FreeTTSEngineCentral");

            // Create a Synthesizer
            Synthesizer synthesizer
                    = Central.createSynthesizer(
                    new SynthesizerModeDesc(Locale.US));

            // Allocate synthesizer
            synthesizer.allocate();

            // Resume Synthesizer
            synthesizer.resume();

            // Speaks the given text
            // until the queue is empty.
            synthesizer.speakPlainText(
                    "Hello my name is kevin", null);
            synthesizer.waitEngineState(
                    Synthesizer.QUEUE_EMPTY);

            // Deallocate the Synthesizer.
            synthesizer.deallocate();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
