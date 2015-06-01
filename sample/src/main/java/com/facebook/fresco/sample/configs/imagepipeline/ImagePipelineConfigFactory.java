/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.facebook.fresco.sample.configs.imagepipeline;


import android.content.Context;
import android.util.Log;

import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.squareup.okhttp.OkHttpClient;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import com.facebook.fresco.sample.configs.ConfigConstants;
import com.facebook.common.logging.FLog;
import android.util.Log;
/**
 * Creates ImagePipeline configuration for the sample app
 */
public class ImagePipelineConfigFactory {
  private static final String IMAGE_PIPELINE_CACHE_DIR = "imagepipeline_cache";

  private static ImagePipelineConfig sImagePipelineConfig;
  private static ImagePipelineConfig sOkHttpImagePipelineConfig;
  private static final  String Tag = "sminger";
  /**
   * Creates config using android http stack as network backend.
   */
  public static ImagePipelineConfig getImagePipelineConfig(Context context) {
    if (sImagePipelineConfig == null) {
        /*
        modify by sminger to verify progressive loading
         */
        ProgressiveJpegConfig pjpegConfig = new ProgressiveJpegConfig() {

            @Override
            public int getNextScanNumberToDecode(int scanNumber) {
                Log.i("ssss", "dddddddddddddd==================================================");
                return scanNumber + 2;
            }

            @Override
            public QualityInfo getQualityInfo(int scanNumber) {
                boolean isGoodEnough = (scanNumber >= 5);
                return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
            }
        };
      ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context)
              .setProgressiveJpegConfig(pjpegConfig);//modify by sminger
      configureCaches(configBuilder, context);
        //configureTrim(configBuilder, context);
      sImagePipelineConfig = configBuilder.build();
    }
    return sImagePipelineConfig;
  }

  /**
   *
   * Creates config using OkHttp as network backed.
   */
  public static ImagePipelineConfig getOkHttpImagePipelineConfig(Context context) {
    if (sOkHttpImagePipelineConfig == null) {
      OkHttpClient okHttpClient = new OkHttpClient();
      ImagePipelineConfig.Builder configBuilder =
        OkHttpImagePipelineConfigFactory.newBuilder(context, okHttpClient);
      configureCaches(configBuilder, context);
      sOkHttpImagePipelineConfig = configBuilder.build();
    }
    return sOkHttpImagePipelineConfig;
  }

  /**
   * Configures disk and memory cache not to exceed common limits
   */
  private static void configureCaches(
      ImagePipelineConfig.Builder configBuilder,
      Context context) {
    final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
        ConfigConstants.MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
        Integer.MAX_VALUE,                     // Max entries in the cache
        ConfigConstants.MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
        Integer.MAX_VALUE,                     // Max length of eviction queue
        Integer.MAX_VALUE);                    // Max cache entry size
    configBuilder
        .setBitmapMemoryCacheParamsSupplier(
            new Supplier<MemoryCacheParams>() {
              public MemoryCacheParams get() {
                return bitmapCacheParams;
              }
            })
        .setMainDiskCacheConfig(
            DiskCacheConfig.newBuilder()
                .setBaseDirectoryPath(context.getApplicationContext().getCacheDir())
                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                .setMaxCacheSize(ConfigConstants.MAX_DISK_CACHE_SIZE)
                .build());
  }

    /*
     configures the trim register to free the memory cache.add by sminger
     */
    private static void configureTrim(
            ImagePipelineConfig.Builder configureBuild,
            Context context ) {
        MemoryTrimmable memoryTrimmable = new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                Log.i("trim", "trim is processing !");
            }
        };
        MemoryTrimmableRegistry MTR = new MemoryTrimmableRegistry() {
            @Override
            public void registerMemoryTrimmable(MemoryTrimmable MT) {

            }

            @Override
            public void unregisterMemoryTrimmable(MemoryTrimmable MT) {

            }
        };
        MTR.registerMemoryTrimmable(memoryTrimmable);
        configureBuild.setMemoryTrimmableRegistry(MTR);
    }
}
