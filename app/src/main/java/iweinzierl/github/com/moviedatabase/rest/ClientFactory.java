package iweinzierl.github.com.moviedatabase.rest;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import org.joda.time.LocalDate;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import iweinzierl.github.com.moviedatabase.rest.client.BackendClient;
import iweinzierl.github.com.moviedatabase.rest.client.SearchClient;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class ClientFactory {

    private static final String BASE_URL_SEARCH = "http://moviedatabase-search.inselhome.org";
    private static final String BASE_URL_BACKEND = "http://moviedatabase-backend.inselhome.org";

    public static SearchClient createSearchClient(Context context) {
        OkHttpClient client = createClient();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL_SEARCH)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .build()
                .create(SearchClient.class);
    }

    public static BackendClient createBackendClient(Context context) {
        OkHttpClient client = createClient();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL_BACKEND)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .build()
                .create(BackendClient.class);
    }

    private static OkHttpClient createClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        client.setReadTimeout(15000, TimeUnit.MILLISECONDS);
        client.interceptors().add(createLoggingInterceptor());

        return client;
    }

    private static HttpLoggingInterceptor createLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    private static Gson createGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                    @Override
                    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                        JsonArray arr = new JsonArray();
                        arr.add(new JsonPrimitive(src.getYear()));
                        arr.add(new JsonPrimitive(src.getMonthOfYear()));
                        arr.add(new JsonPrimitive(src.getDayOfMonth()));
                        return arr;
                    }
                })
                .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        JsonArray arr = json.getAsJsonArray();
                        return new LocalDate(
                                arr.get(0).getAsInt(),
                                arr.get(1).getAsInt(),
                                arr.get(2).getAsInt()
                        );
                    }
                })
                .create();
    }
}
