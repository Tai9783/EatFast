
plugins {
        // Plugin cho ứng dụng Android
        alias(libs.plugins.android.application)

        // Plugin Kotlin cho Android
        alias(libs.plugins.kotlin.android)

        // Plugin để kích hoạt Google Services (Firebase), nhưng chưa áp dụng ở đây (apply false)
        // Plugin này sẽ được apply thủ công ở cuối file với apply(plugin = "...") để Firebase hoạt động
        alias(libs.plugins.navigation.safeargs) // add dòng này để dùng pthuc truyen du tu fragment sang fragment
        // Kích hoạt annotation processor cho Glide (hoặc các thư viện cần annotation processing)
        id("kotlin-kapt")
}

    android {
        namespace = "com.example.foodorderapp"
        compileSdk = 35

        defaultConfig {
            applicationId = "com.example.foodorderapp"
            minSdk = 24
            targetSdk = 35
            versionCode = 1
            versionName = "1.0"
            vectorDrawables.useSupportLibrary = true//
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        kotlinOptions {
            jvmTarget = "11"
        }

        buildFeatures {
            // Bật ViewBinding để thao tác với View dễ dàng mà không cần findViewById
            viewBinding = true
        }
        // 🚀 thêm block này để bỏ qua lint khi build APK
        lint {
            abortOnError = false
            checkReleaseBuilds = false
        }
    }

    dependencies {
        // Firebase BOM để đồng bộ phiên bản các thư viện Firebase
        implementation(platform(libs.firebase.bom))

        // Firebase Analytics (theo dõi hành vi người dùng)
        implementation(libs.firebase.analytics)

        // Các thư viện cơ bản của Android
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)

        // Thư viện tạo avatar hình tròn
        implementation(libs.hdodenhof.circleimageview)

        // Firebase Realtime Database
        implementation(libs.firebase.database)
        implementation(libs.firebase.firestore.ktx)
        implementation(libs.androidx.lifecycle.livedata.ktx)
        implementation(libs.androidx.room.common.jvm)
        implementation(libs.androidx.room.runtime.android)

        // Thư viện kiểm thử
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)

        // Glide: thư viện tải ảnh từ internet hoặc Firebase Storage
        implementation(libs.glide)

        // Kapt để xử lý annotation của Glide (GlideApp)
        kapt(libs.glideCompiler)

        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.room.ktx)
        kapt(libs.androidx.room.compiler)

        //Cung cấp khả năng điều hướng cho các fragment
        implementation(libs.navigation.fragment)
        implementation(libs.navigation.ui)

        implementation(libs.firebase.firestore.ktx.v2491)

        //dùng Firebase auth để xác thực tài khoản
        implementation(libs.firebase.auth)
        // BoM (quản lý phiên bản đồng bộ)
        implementation(platform(libs.firebase.bom))

        implementation(libs.firebase.auth.ktx)
        implementation(libs.play.services.auth)
        // dùng để tùy chỉnh giao kích thước màn hình(Tùy chỉnh món ăn ) về nửa màn hình ....
        implementation(libs.material)



    }

    // Áp dụng plugin Google Services (bắt buộc khi dùng Firebase)
    // Firebase cần plugin này để đọc file google-services.json và cấu hình dịch vụ tương ứng
    apply(plugin = "com.google.gms.google-services")
