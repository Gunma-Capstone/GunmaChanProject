# Speech Recognition

## Status

Currently the speech recognition functionality uses the `DroidSpeech` [library](https://github.com/christinaeverman/DroidSpeech) which is a fork of [this repo](https://github.com/vikramezhil/DroidSpeech). 

It works, but often lags and is unreliable. WiFi is required for the game to work and users have to wait for a sound before the game starts listening for the word to be spoken. Sometimes the game never even starts listening and the user has to toggle pause to cause the recognition to start again.

## Context

In December 2019, we had an issue where `DroidSpeech` stopped working with a new Kindle Fire update. Christina was able to fix it, which required us to change our implementation to depend on [her fork](https://github.com/christinaeverman/DroidSpeech) rather than the [original repo](https://github.com/vikramezhil/DroidSpeech). Below are her notes while testing and resolving the error.

## Consequences

The speech recognition library is very unstable because the [original repo](https://github.com/vikramezhil/DroidSpeech) was last updated in 2017 and every android update could cause another issue like one we faced. It would be more stable to use a more popular and current API. Because the whole game is built off of this library, it will be difficult and time-consuming to change. 

## Christina's Error Notes

### The Problem

- The problem is related to the creation of the DroidSpeech object from the voice recognition code.
- More specifically, the problem occurs in getVoiceDetailsIntent() in RecognizerIntent.java when creating a new Intent object in the DroidSpeech class.
- This still works on the Android Studio emulator.
- The failing devices seem to have a more up to date version of FireOS than the working devices.

### Testing 

- Installed the app from other PCs onto the failing device.
- Installed the app from Christina’s PC onto other devices and the app launched successfully.
- Factory reset device.
- Uninstalled and reinstalled Android Studio.
- Placed breakpoints around the line of code that is failing, to narrow down what the problem is exactly
- Since the Kindle app store does not offer Google apps, I researched online to see if I could find some way to install the Google Play app store onto the device. I downloaded an installation package and ran a script on my PC to install Google Play and set the device’s permissions. Then I installed various Google apps that may handle Intent in the DroidSpeech class including Google Chrome and Google search, but neither of these installations helped to launch the app successfully.
- Turned WiFi off.
- Instead of installing the app from Android Studio directly, I built the Android Studio project on my PC, uploaded the build files to Google Drive, and then downloaded the files onto the device and attempted to install the app that way but the install was unsuccessful.

### The Solution

In the GitHub repo containing the voice recognition library, a sample Android app provided. I have installed the sample app on the Fire 7 but it also fails to launch in the same way our app does. I tried to modify the voice recognition library code based on [this possible solution](https://github.com/farhanbombay/DroidSpeech/commit/d322c1b35c37f1d4b9774eef84acd6d31ddd8a84) and the sample app then launched successfully. If I can modify the voice recognition library for our app as I did with the sample app, then our app may launch. The problem with this is the voice recognition library files in our project are read-only as they are all contained within a .jar package.

### Other Possible Solutions that were not Tested
- The voice recognition code added by the previous team relies on something from the device, and that something from the device is missing upon updating FireOS to a newer version.
- The author of the voice recognition code forgot to initialize something in their code and on the majority of devices this is fine, but may not be okay in some cases.
- Try launching the Intent with some sample code.
