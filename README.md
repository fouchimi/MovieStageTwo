# MovieStageTwo
MovieStage stage two is an extension of <a href="https://github.com/fouchimi/MovieStageOne">MovieStageOne</a> which supports both design for phone and tablets devices. In order to get this app up and running you will need to provide it with an API key from www.themoviedb.org website and substitute the valute of your API key in Constants.java file located in the entities directory. 

<h2>Installation</h2>
$git clone https://github.com/fouchimi/MovieStageTwo.git

On Linux and Mac OS, you can open the project on Android Studio and run <b>./gradlew assembleDebug</b> and <b>adb -d install app/{path-to-your-apk}</b> which is often <b>app/build/outputs/apk/{apk_file}</b>.

On Windows, you can run this command instead <b>gradle.bat assembleDebug</b> and <b>adb -d install app/{path-to-your-apk}</b>

Alternatively you can simply click on the run button of the IDE and those steps will be ran automatically and load the application on your mobile or virtual device.
