package com.discursive.nexus.lightning;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.access.Action;
import org.sonatype.nexus.proxy.item.AbstractStorageItem;
import org.sonatype.nexus.proxy.item.StorageFileItem;
import org.sonatype.nexus.proxy.repository.ProxyRepository;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.proxy.repository.RequestProcessor;
import org.sonatype.plexus.appevents.ApplicationEventMulticaster;

@Named( "lightning" )
public class LightningRequestProcessor
    implements RequestProcessor
{
	
	private static Logger log = LoggerFactory.getLogger( LightningRequestProcessor.class );
	
    @Inject
    private ApplicationEventMulticaster applicationEventMulticaster;

/*    @Inject
    private @Named( "XY" )
    VirusScanner virusScanner; */

    // @Inject
    // private @Named("A") CommonDependency commonDependency;

    public boolean process( Repository repository, ResourceStoreRequest request, Action action )
    {
    	log.debug( "Process: " + repository.toString() + " , " + request.toString() + " , " + action.toString() );
    	
        // Check dependency
        // System.out.println( "VirusScannerRequestProcessor --- CommonDependency data: " + commonDependency.getData()
        // );

        // don't decide until have content
        return true;
    }

    public boolean shouldProxy( ProxyRepository repository, ResourceStoreRequest request )
    {
    	log.debug( "Should proxy? " + request.toString() + " , " + repository.toString() );
    	
        // don't decide until have content
        return true;
    }

    public boolean shouldCache( ProxyRepository repository, AbstractStorageItem item )
    {
    	//log.debug( "Should Cache? " + item.toString() + ", " + repository.toString() );
    	
    	//log.debug( "Item stuff: " + item.getClass().toString() );
    	//log.debug( "Item name: " + item.getName() );
    	
    	if ( item instanceof StorageFileItem )
        {
            StorageFileItem file = (StorageFileItem) item;

            // do a virus scan
            //boolean hasVirus = virusScanner.hasVirus( file );

            /*if ( hasVirus )
            {
                applicationEventMulticaster.notifyEventListeners( new InfectedItemFoundEvent( item
                    .getRepositoryItemUid().getRepository(), file ) );
            }*/

            // If the file is a POM, let's parse the thing
            if( file.getName().endsWith( ".pom" ) ) {

            	
            	log.debug( "This file is a POM FILE!!!" );
            	
            	MavenXpp3Reader reader = new MavenXpp3Reader();
            	Model model = null;
            	try {
					model = reader.read( file.getInputStream() );
				} catch (IOException e) {
					log.error( "IOException whilst parsing a POM file." );
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					log.error( "XmlPullParserException whilst parsing a POM file." );
				}
            	
            	if( model != null ) {
            		log.debug( "Artifact groupId: " + model.getGroupId() );
            		log.debug( "Artifact artifactId: " + model.getArtifactId() );
            		log.debug( "Artifact version: " + model.getVersion() );
            		log.debug( "Artifact Packaging: " + model.getPackaging() );
            		
            		for( Dependency dep : model.getDependencies() ) {
            			log.debug( "Dependency groupId: " + dep.getGroupId() );
            			log.debug( "Dependency artifactId: " + dep.getArtifactId() );
            			log.debug( "Dependency version: " + dep.getVersion() );
            			log.debug( "Dependency classifier: " + dep.getClassifier() );
            			log.debug( "Dependency packaging: " + dep.getType() );
            			log.debug( "Dependency scope: " + dep.getScope() );
            		}
            		
            	}
            	
            	
            	
            	
            }
            
            
            return true;
        }
        else
        {
            return true;
        }
    }

}
