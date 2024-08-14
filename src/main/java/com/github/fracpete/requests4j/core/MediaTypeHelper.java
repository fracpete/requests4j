/*
 * MediaTypeHelper.java
 * Copyright (C) 2013-2024 University of Waikato, Hamilton, New Zealand
 */
package com.github.fracpete.requests4j.core;

import okhttp3.MediaType;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Helper class for media types.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class MediaTypeHelper {

  public final static MediaType OCTECT_STREAM = MediaType.parse(org.apache.tika.mime.MediaType.OCTET_STREAM.toString());

  public final static MediaType TEXT_PLAIN = MediaType.parse(org.apache.tika.mime.MediaType.TEXT_PLAIN.toString());

  public final static MediaType APPLICATION_JSON = MediaType.parse("application/json");

  public final static MediaType APPLICATION_JSON_UTF8 = MediaType.parse("application/json; charset=utf-8");

  /**
   * Tries to determine the media type of a file by checking its magic bytes.
   * Taken from here:
   * <a href="http://stackoverflow.com/a/16626396" target="_blank">http://stackoverflow.com/a/16626396</a>.
   *
   * @param file	the file to check
   * @return		the MIME type, {@link org.apache.tika.mime.MediaType#OCTET_STREAM} if it fails
   */
  public static MediaType getMediaType(File file) {
    return MediaType.parse(MediaTypeHelper.getMediaType(file.getAbsolutePath()).toString());
  }

  /**
   * Tries to determine the media type of a file by checking its magic bytes.
   * Taken from here:
   * <a href="http://stackoverflow.com/a/16626396" target="_blank">http://stackoverflow.com/a/16626396</a>.
   *
   * @param filename	the file to check
   * @return		the MIME type, {@link org.apache.tika.mime.MediaType#OCTET_STREAM} if it fails
   */
  public static MediaType getMediaType(String filename) {
    org.apache.tika.mime.MediaType 	result;
    FileInputStream             	fis;
    BufferedInputStream 		bis;
    AutoDetectParser 			parser;
    Detector 				detector;
    Metadata 				md;

    fis = null;
    bis = null;
    try {
      fis      = new FileInputStream(filename);
      bis      = new BufferedInputStream(fis);
      parser   = new AutoDetectParser();
      detector = parser.getDetector();
      md       = new Metadata();
      md.add(TikaCoreProperties.RESOURCE_NAME_KEY, filename);
      result = detector.detect(bis, md);
      bis.close();
      return MediaType.parse(result.toString());
    }
    catch (Exception e) {
      return MediaType.parse(org.apache.tika.mime.MediaType.OCTET_STREAM.toString());
    }
    finally {
      if (bis != null) {
        try {
          bis.close();
        }
        catch (Exception e) {
        }
      }
      if (fis != null) {
        try {
          fis.close();
        }
        catch (Exception e) {
        }
      }
    }
  }
}
