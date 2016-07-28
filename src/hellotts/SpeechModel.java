/*
 * Copyright © 2016 Toru Takahahshi. All rights reserved.
 */
package hellotts;

import java.beans.PropertyVetoException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;

/**
 *
 * @author toru
 */
public class SpeechModel {
    private SynthesizerModeDesc desc;
    private Synthesizer synthesizer;
    
    public void init(String voiceName) throws EngineException, AudioException, PropertyVetoException {
        if (desc != null) {
            return;
        }
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        desc = new SynthesizerModeDesc(Locale.US);
        Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
        synthesizer = Central.createSynthesizer(desc);
        synthesizer.allocate();
        synthesizer.resume();
        SynthesizerModeDesc smd = (SynthesizerModeDesc) synthesizer.getEngineModeDesc();
        Optional<Voice> voice = Arrays.stream(smd.getVoices())
                .filter(v -> v.getName().equals(voiceName))
                .findFirst();
        synthesizer.getSynthesizerProperties().setVoice(voice.get());
    }
    
    public void terminate() {
        try {
            synthesizer.deallocate();
        } catch (EngineException ex) {
            // do nothing
        }
    }
    
    public void doSpeech(String text) throws InterruptedException {
        synthesizer.speakPlainText(text, null);
        synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
    }
    
    private SpeechModel() {
    }
    
    public static SpeechModel getInstance() {
        return SpeechModelHolder.INSTANCE;
    }
    
    private static class SpeechModelHolder {
        private static final SpeechModel INSTANCE = new SpeechModel();
    }
}
