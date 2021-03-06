package org.rfcx.guardian.encode.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.rfcx.guardian.audio.flac.FLAC_FileEncoder;
import org.rfcx.guardian.audio.mp3.Mp3AudioEncoder;
import org.rfcx.guardian.audio.opus.OpusAudioEncoder;
import org.rfcx.guardian.encode.RfcxGuardian;
import org.rfcx.guardian.utility.FileUtils;
import org.rfcx.guardian.utility.audio.RfcxAudio;
import org.rfcx.guardian.utility.rfcx.RfcxLog;

import android.util.Log;

public class AudioEncodeUtils {

	private static final String logTag = "Rfcx-"+RfcxGuardian.APP_ROLE+"-"+AudioEncodeUtils.class.getSimpleName();
	
	public static int encodeAudioFile(File preEncodeFile, File postEncodeFile, String encodeCodec, int encodeSampleRate, int encodeBitRate, int encodeQuality) {
		
		int encodeOutputBitRate = -1;
		
		if (preEncodeFile.exists()) {
			try {
				if (encodeCodec.equalsIgnoreCase("opus")) {
					
					OpusAudioEncoder opusEncoder = new OpusAudioEncoder();
					String encStatus = opusEncoder.transcode(preEncodeFile, postEncodeFile, encodeBitRate, encodeQuality);
					if (encStatus.equalsIgnoreCase("OK")) { encodeOutputBitRate = encodeBitRate; }
					Log.d(logTag, "OPUS Encoding Complete: "+encStatus);
					
				} else if (encodeCodec.equalsIgnoreCase("mp3")) {
					
					Mp3AudioEncoder mp3Encoder = new Mp3AudioEncoder();
					String encStatus = mp3Encoder.transcode(preEncodeFile, postEncodeFile, encodeBitRate, encodeQuality);
					if (encStatus.equalsIgnoreCase("OK")) { encodeOutputBitRate = encodeBitRate; }
					Log.d(logTag, "MP3 Encoding Complete: "+encStatus);
					
				} else if (encodeCodec.equalsIgnoreCase("flac")) {
					
					FLAC_FileEncoder flacEncoder = new FLAC_FileEncoder();
					flacEncoder.adjustAudioConfig(encodeSampleRate, RfcxAudio.AUDIO_SAMPLE_SIZE, RfcxAudio.AUDIO_CHANNEL_COUNT);
					FLAC_FileEncoder.Status encStatus = flacEncoder.encode(preEncodeFile, postEncodeFile);
					if (encStatus == FLAC_FileEncoder.Status.FULL_ENCODE) { encodeOutputBitRate = 0; }
					Log.d(logTag, "FLAC Encoding Complete: "+encStatus.name());
					
				} else {
					
					FileUtils.copy(preEncodeFile, postEncodeFile);
					encodeOutputBitRate = encodeBitRate;
					
				}
			} catch (Exception e) {
				RfcxLog.logExc(logTag, e);
			}
		}
		
		return encodeOutputBitRate;
	}
	
	public static void cleanupEncodeDirectory(List<String[]> queuedForEncode) {
		
		ArrayList<String> filesQueuedForEncode = new ArrayList<String>();
		for (String[] queuedRow : queuedForEncode) {
			filesQueuedForEncode.add(queuedRow[9]);
		}
		
		FileUtils.deleteDirectoryContents(RfcxAudio.encodeDir(), filesQueuedForEncode);
	}
	
}
