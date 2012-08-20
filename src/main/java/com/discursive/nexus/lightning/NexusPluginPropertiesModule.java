package com.discursive.nexus.lightning;

import java.io.File;
import java.io.FileInputStream;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.guice.bean.binders.ParameterKeys;

import com.google.inject.AbstractModule;

public class NexusPluginPropertiesModule extends AbstractModule {

	private static Logger log = LoggerFactory
			.getLogger(NexusPluginPropertiesModule.class);

	private final String fileName;

	public NexusPluginPropertiesModule(String id) {
		this.fileName = id + ".properties";
	}

	@Override
	protected void configure() {
		InjectableProperties properties = new InjectableProperties();
		//
		// This tells Guice that upon successful Injector creation to perform
		// injection upon the InjectableProperties
		// instance that we created above. When that happens the loadProperties
		// method below with the @Inject will
		// be invoked and cause the properties to be loaded. This will happen
		// before the Injector can be used
		// to lookup any instances.
		//
		requestInjection(properties);
		bind(ParameterKeys.PROPERTIES).toInstance(properties);
	}

	private class InjectableProperties extends AbstractMap<String, String> {

		private Map<String, String> properties = Collections.emptyMap();

		@Inject
		public void loadProperties(
				@Named("${application-conf}") String configurationDirectory) {
			try {
				File file = new File(configurationDirectory, fileName)
						.getCanonicalFile();
				if (file.exists()) {
					Properties props = new Properties();
					props.load(new FileInputStream(file));
					properties = (Map) props;
				} else {
					file.createNewFile();

				}
			} catch (Exception e) {
				log.error("Error loading or creating properties file: "
						+ configurationDirectory + ", " + fileName);
			}
		}

		@Override
		public Set<Entry<String, String>> entrySet() {
			return properties.entrySet();
		}
	}
}