package nl.enjarai.doabarrelroll.platform;

import nl.enjarai.doabarrelroll.platform.services.ClientNetworkUtils;
import nl.enjarai.doabarrelroll.platform.services.PlatformHelper;
import nl.enjarai.doabarrelroll.platform.services.ServerNetworkUtils;

import java.util.ServiceLoader;

// Service loaders are a built-in Java feature that allow us to locate implementations of an interface that vary from one
// environment to another. In the context of MultiLoader we use this feature to access a mock API in the common code that
// is swapped out for the platform specific implementation at runtime.
public class Services {
    // In this example we provide a platform helper which provides information about what platform the mod is running on.
    // For example this can be used to check if the code is running on Forge vs Fabric, or to ask the modloader if another
    // mod is loaded.
    public static final PlatformHelper PLATFORM = load(PlatformHelper.class);
    public static final ServerNetworkUtils SERVER_NET = load(ServerNetworkUtils.class);
    public static final ClientNetworkUtils CLIENT_NET = load(ClientNetworkUtils.class);

    // This code is used to load a service for the current environment. Your implementation of the service must be defined
    // manually by including a text file in META-INF/services named with the fully qualified class name of the service.
    // Inside the file you should write the fully qualified class name of the implementation to load for the platform. For
    // example our file on Forge points to ForgePlatformHelper while Fabric points to FabricPlatformHelper.
    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }
}