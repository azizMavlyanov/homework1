package ru.digitalhabbits.homework1.service;

import com.google.common.reflect.ClassPath;
import org.slf4j.Logger;
import ru.digitalhabbits.homework1.plugin.PluginInterface;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.stream;
import static org.slf4j.LoggerFactory.getLogger;

public class PluginLoader {
    private static final Logger logger = getLogger(PluginLoader.class);

    private static final String PLUGIN_EXT = "jar";
    private static final String PACKAGE_TO_SCAN = "ru.digitalhabbits.homework1.plugin";

    @Nonnull
    public List<Class<? extends PluginInterface>> loadPlugins(@Nonnull String pluginDirName) {
        final List<Class<? extends PluginInterface>> pluginList = new ArrayList<>();
        final String currentDir = System.getProperty("user.dir");
        final File pluginDir = new File(currentDir + "/" + pluginDirName);
        final List<File> jarFiles = stream(pluginDir.list((dir, name) -> name.endsWith(PLUGIN_EXT)))
                .map(jarFile -> new File(pluginDir + "/" + jarFile))
                .collect(Collectors.toList());

        final URL[] urls = new URL[jarFiles.size()];

        for (int i = 0; i < jarFiles.size(); i++) {
            try {
                urls[i] = jarFiles.get(i).toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        final ClassLoader classLoader = new URLClassLoader(urls);

        try {
            ClassPath.from(classLoader).getTopLevelClasses(PACKAGE_TO_SCAN).stream()
                    .forEach(cl -> {
                        try {
                            final Class<?> loadedClass = classLoader.loadClass(cl.getName());
                            if (!loadedClass.isInterface() && PluginInterface.class.isAssignableFrom(loadedClass)) {
                                pluginList.add((Class<? extends PluginInterface>) loadedClass);
                            }
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return newArrayList(pluginList);
    }
}
