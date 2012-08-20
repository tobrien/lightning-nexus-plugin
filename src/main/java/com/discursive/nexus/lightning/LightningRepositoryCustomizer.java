package com.discursive.nexus.lightning;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.configuration.ConfigurationException;
import org.sonatype.nexus.plugins.RepositoryCustomizer;
import org.sonatype.nexus.proxy.repository.ProxyRepository;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.proxy.repository.RequestProcessor;

public class LightningRepositoryCustomizer
    implements RepositoryCustomizer
{
	
	private static Logger log = LoggerFactory.getLogger( LightningRepositoryCustomizer.class );
	
    @Inject
    private @Named( "lightning" )
    RequestProcessor lightningRequestProcessor;

    public boolean isHandledRepository( Repository repository )
    {
    	log.debug( "Is handled repo? " + repository.toString() );
    	
        // handle proxy reposes only
        return repository.getRepositoryKind().isFacetAvailable( ProxyRepository.class );
    }

    public void configureRepository( Repository repository )
        throws ConfigurationException
    {
    	log.debug( "Configure repository: " + repository.toString() );
    	
        repository.getRequestProcessors().put( "lightning", lightningRequestProcessor );
    }

}
