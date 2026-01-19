package ru.ugrasu.eljunior.data.repository;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import ru.ugrasu.eljunior.data.api.MoodleApi;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<MoodleApi> moodleApiProvider;

  public AuthRepository_Factory(Provider<Context> contextProvider,
      Provider<MoodleApi> moodleApiProvider) {
    this.contextProvider = contextProvider;
    this.moodleApiProvider = moodleApiProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(contextProvider.get(), moodleApiProvider.get());
  }

  public static AuthRepository_Factory create(Provider<Context> contextProvider,
      Provider<MoodleApi> moodleApiProvider) {
    return new AuthRepository_Factory(contextProvider, moodleApiProvider);
  }

  public static AuthRepository newInstance(Context context, MoodleApi moodleApi) {
    return new AuthRepository(context, moodleApi);
  }
}
