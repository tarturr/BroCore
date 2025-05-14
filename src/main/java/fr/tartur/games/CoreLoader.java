package fr.tartur.games;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

/**
 * Loads some Maven dependencies required at runtime.
 */
public class CoreLoader implements PluginLoader {
    
    @Override
    public void classloader(PluginClasspathBuilder classpath) {
        final MavenLibraryResolver resolver = new MavenLibraryResolver();
        
        resolver.addRepository(new RemoteRepository.Builder("central", "default", "https://repo1.maven.org/maven2/").build());
        resolver.addDependency(new Dependency(new DefaultArtifact("com.zaxxer:HikariCP:6.3.0"), null));
        
        classpath.addLibrary(resolver);
    }
    
}
