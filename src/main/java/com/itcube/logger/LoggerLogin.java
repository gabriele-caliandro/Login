package com.itcube.logger;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class LoggerLogin {
  public static final Logger loggerLogin =
          (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.itcube");
}
