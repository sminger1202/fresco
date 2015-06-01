package in.srain.demos.fresco;

import com.facebook.common.util.ByteConstants;

/**
 * Created by sminger on 15-5-25.
 */
public class ConfigConstants {
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();

    public static final int MAX_DISK_CACHE_SIZE = 40 * ByteConstants.MB;
    public static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;
}
