/* 
 * CTS2 based Terminology Server and Terminology Browser
 * Copyright (C) 2014 FH Dortmund: Peter Haas, Robert Muetzner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fhdo.logging;

import java.io.File;
import org.apache.log4j.*;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Logger-Klasse.
 * Einfach initialisieren mit "getInstance().getLogger()".
 *
 * @author Robert Muetzner
 */
public class Logger4j
{
  /** Logging-Konfigurationsdatei */
  private static final String LOG4J_CONFIG_FILE = "EPD-Demo.log4j.cfg.xml";

  private static final String LOG4J_LOG_FILE = "EPD-Demo.log";

  /** Der Logger*/
  private Logger logger = null;

  /** Singleton-Instanz*/
  private static Logger4j instance = null;

  public static Logger4j getInstance()
  {
    if(instance == null)
      instance = new Logger4j();
    return instance;
  }

  /**
   * Konstruktor. Privat wg. Singleton
   *
   */
  private Logger4j()
  {
    initLogger();
  }

  public Logger getLogger()
  {
    return logger;
  }

  /**
   * Initialisiert den Logger. Liest die Eigenschaften aus der XML-Configdatei.
   * Wenn diese nicht vorhanden ist, werden Standardeinstellungen benutzt.
   *
   */
  private void initLogger()
  {
    System.out.println("initLogger()");
    
    try
    {
      
      String sFile = System.getProperty("catalina.base") + "/conf/" + LOG4J_CONFIG_FILE;
      
      
      File file = new File(sFile);

      if (file.exists() == false)
      {
        
        // Standard-Logger benutzen (keine Konfigurationsdatei gefunden)
        logger = Logger.getRootLogger();
        // Layout & Appender
        SimpleLayout layout = new SimpleLayout();
        
        String sFileLogs = System.getProperty("catalina.base") + "/logs/" + LOG4J_LOG_FILE;
        FileAppender fileAppender = new RollingFileAppender(layout, sFileLogs, true);
        
        ConsoleAppender consoleAppender = new ConsoleAppender(layout);
        
        // ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF:
        logger.setLevel( Level.DEBUG );
        consoleAppender.setThreshold(Level.DEBUG);
        fileAppender.setThreshold(Level.DEBUG);
        
        logger.addAppender(consoleAppender);
        logger.addAppender(fileAppender);

        // Meldung ausgeben
        logger.info("Konfigurationsdatei '" + (sFile) +
          "'nicht gefunden, benutze Standardeigenschaften!");
      }
      else
      {
        // Konfiguration aus der Konfigurationsdatei verwenden
        DOMConfigurator.configure(sFile);

        logger = Logger.getRootLogger();
        logger.info("Logger mit Eigenschaften aus '" + (sFile) +
          "' erfolgreich initialisiert!");
      }

    }
    catch (Exception ex)
    {
      System.out.println("Fehler bei Logger-Initialisierung: " + ex.getMessage());
      System.err.println("Fehler bei Logger-Initialisierung: " + ex.getMessage());
    }
  }
}
