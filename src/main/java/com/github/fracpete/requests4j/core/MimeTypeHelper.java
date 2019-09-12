/*
 * MimeTypeHelper.java
 * Copyright (C) 2013-2019 University of Waikato, Hamilton, New Zealand
 */
package com.github.fracpete.requests4j.core;

import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Helper class for mime types.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 */
public class MimeTypeHelper {

  /**
   * Tries to determine the MIME type of a file by checking its magic bytes.
   * Taken from here:
   * <a href="http://stackoverflow.com/a/16626396" target="_blank">http://stackoverflow.com/a/16626396</a>.
   *
   * @param file	the file to check
   * @return		the MIME type, {@link MediaType#OCTET_STREAM} if it fails
   */
  public static MediaType getMimeType(File file) {
    return MimeTypeHelper.getMimeType(file.getAbsolutePath());
  }

  /**
   * Tries to determine the MIME type of a file by checking its magic bytes.
   * Taken from here:
   * <a href="http://stackoverflow.com/a/16626396" target="_blank">http://stackoverflow.com/a/16626396</a>.
   *
   * @param filename	the file to check
   * @return		the MIME type, {@link MediaType#OCTET_STREAM} if it fails
   */
  public static MediaType getMimeType(String filename) {
    MediaType 			result;
    FileInputStream             fis;
    BufferedInputStream 	bis;
    AutoDetectParser 		parser;
    Detector 			detector;
    Metadata 			md;

    fis = null;
    bis = null;
    try {
      fis      = new FileInputStream(filename);
      bis      = new BufferedInputStream(fis);
      parser   = new AutoDetectParser();
      detector = parser.getDetector();
      md       = new Metadata();
      md.add(Metadata.RESOURCE_NAME_KEY, filename);
      result = detector.detect(bis, md);
      bis.close();
      return result;
    }
    catch (Exception e) {
      return MediaType.OCTET_STREAM;
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
