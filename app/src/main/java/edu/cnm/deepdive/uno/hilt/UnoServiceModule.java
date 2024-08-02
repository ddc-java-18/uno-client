package edu.cnm.deepdive.uno.hilt;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.uno.R;
import edu.cnm.deepdive.uno.service.UnoServiceLongProxy;
import edu.cnm.deepdive.uno.service.UnoServiceProxy;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class UnoServiceModule {

  /**
   * Default initialization constructor for the {@code UnoServiceModule}
   */
  UnoServiceModule() {

  }

  /**
   * Provides an UnoServiceProxy as a singleton using Hilt/Dagger dependency injection.
   *
   * <p>
   *   This method initializes the app's GSON serializer, OkHttp web client, and provides them for
   *   the creation of a Retrofit object.
   * </p>
   *
   * @param context the app's ApplicationContext.
   * @return UnoServiceProxy.
   */
  @Provides
  @Singleton
  public UnoServiceProxy provideProxy(@ApplicationContext Context context) {
    Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create();
    String loggingLevelProperty = context.getString(R.string.logging_level).toUpperCase();
    Level loggingLevel = Level.valueOf(loggingLevelProperty);
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(loggingLevel);

    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(logging)
        .build();

    Retrofit retrofit = new Retrofit.Builder()
        .client(client)
        .baseUrl(context.getString(R.string.base_url))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build();

    return retrofit.create(UnoServiceProxy.class);
  }

  @Provides
  @Singleton
  public UnoServiceLongProxy provideLongProxy(@ApplicationContext Context context) {
    Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create();
    String loggingLevelProperty = context.getString(R.string.logging_level).toUpperCase();
    Level loggingLevel = Level.valueOf(loggingLevelProperty);
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(loggingLevel);

    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(0, TimeUnit.SECONDS)
        .writeTimeout(0, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.SECONDS)
        .callTimeout(15, TimeUnit.SECONDS)
        .build();

    Retrofit retrofit = new Retrofit.Builder()
        .client(client)
        .baseUrl(context.getString(R.string.base_url))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build();

    return retrofit.create(UnoServiceLongProxy.class);
  }


}
