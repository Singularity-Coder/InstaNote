# InstaNote
A dead simple notes App! In this App you can add text notes and add images from Pixabay.

![alt text](https://github.com/Singularity-Coder/Notez/blob/main/assets/p1.jpg)
![alt text](https://github.com/Singularity-Coder/Notez/blob/main/assets/p2.jpg)
![alt text](https://github.com/Singularity-Coder/Notez/blob/main/assets/p3.jpg)
![alt text](https://github.com/Singularity-Coder/Notez/blob/main/assets/p4.jpg)

## Tech stack & Open-source libraries
- Minimum SDK level 21
-  [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [LiveData](https://developer.android.com/topic/libraries/architecture/livedatahttps://developer.android.com/topic/libraries/architecture/livedata) for asynchronous.
- Jetpack
  - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - DataBinding: Binds UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
  - Room: Constructs Database by providing an abstraction layer over SQLite to allow fluent database access.
  - [Hilt](https://dagger.dev/hilt/): for dependency injection.
  - WorkManager: WorkManager allows you to schedule work to run one-time or repeatedly using flexible scheduling windows.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - Repository Pattern
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Construct the REST APIs and paging network data.
- [gson](https://github.com/google/gson): A Java serialization/deserialization library to convert Java Objects into JSON and back.
- [Material-Components](https://github.com/material-components/material-components-android): Material design components for building ripple animation, and CardView.
- [jsoup](https://mvnrepository.com/artifact/org.jsoup/jsoup): jsoup is a Java library that simplifies working with real-world HTML and XML.
- [Coil](https://github.com/coil-kt/coil): Image loading for Android and Compose Multiplatform.
- [Lottie](https://github.com/airbnb/lottie-android): Render After Effects animations natively on Android and iOS, Web, and React Native.
- [zxing-android-embedded](https://github.com/journeyapps/zxing-android-embedded): Barcode scanner library for Android, based on the ZXing decoder.

## Architecture
![alt text](https://github.com/Singularity-Coder/Notez/blob/main/assets/arch.png)

This App is based on the MVVM architecture and the Repository pattern, which follows the [Google's official architecture guidance](https://developer.android.com/topic/architecture).

The overall architecture of this App is composed of two layers; the UI layer and the data layer. Each layer has dedicated components and they have each different responsibilities.