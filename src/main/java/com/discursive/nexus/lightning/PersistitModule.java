package com.discursive.nexus.lightning;

import javax.inject.Named;

import com.google.inject.AbstractModule;

@Named("persistit")
public class PersistitModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new NexusPluginPropertiesModule("persistit"));
  }
}