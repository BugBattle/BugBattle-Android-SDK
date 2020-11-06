package bugbattle.io.bugbattle;

import android.app.Application;
import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import bugbattle.io.bugbattle.controller.BugBattleActivationMethod;
import bugbattle.io.bugbattle.controller.BugBattleNotInitialisedException;
import bugbattle.io.bugbattle.model.FeedbackModel;
import bugbattle.io.bugbattle.controller.StepsToReproduce;
import bugbattle.io.bugbattle.service.ScreenshotTaker;
import bugbattle.io.bugbattle.service.ShakeGestureDetector;
import bugbattle.io.bugbattle.view.Feedback;


public class BugBattle {
    private static BugBattle instance;
    private ShakeGestureDetector shakeGestureDetector;

    private BugBattle(String sdkKey, BugBattleActivationMethod activationMethod, Application application) {
        FeedbackModel.getInstance().setContext(application.getApplicationContext());
        FeedbackModel.getInstance().setSdkKey(sdkKey);
        //CLEAR logcat
        try {
            Runtime.getRuntime().exec("logcat - c");
        }catch (Exception e) {
            System.out.println(e);
        }
        if (activationMethod == BugBattleActivationMethod.SHAKE) {
            FeedbackModel.getInstance().setShakeGestureDetector(new ShakeGestureDetector(application.getApplicationContext()));
        }
    }

    /**
     * Initialises the Bugbattle SDK.
     *
     * @param application      The application (this)
     * @param sdkKey           The SDK key, which can be found on dashboard.bugbattle.io
     * @param activationMethod Activation method, which triggers a new bug report.
     */
    public static BugBattle initialise(String sdkKey, BugBattleActivationMethod activationMethod, Application application) {
        if (instance == null) {
            instance = new BugBattle(sdkKey, activationMethod, application);
        }
        return instance;
    }

    /**
     * Manually start the bug reporting workflow. This is used, when you use the activation method "NONE".
     *
     * @throws BugBattleNotInitialisedException thrown when BugBattle is not initialised
     */
    public static void startBugReporting() throws BugBattleNotInitialisedException {
        if (instance != null) {
            try {
                new ScreenshotTaker().takeScreenshot();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new BugBattleNotInitialisedException("BugBattle is not initialised");
        }
    }

    /**
     * Starts the bug reporting with a custom screenshot attached.
     * @param bitmap the image will be used instead of the current
     */
    public static void startBugReporting(Bitmap bitmap) {
        FeedbackModel.getInstance().setScreenshot(bitmap);
    }

    /**
     * Track a step to add more information to the bug report
     *
     * @param type Type of the step. (for eg. Button)
     * @param data Custom data associated with the step.
     * @throws JSONException
     */
    public static void trackStep(String type, String data) {
        StepsToReproduce.getInstance().setStep(type, data);
    }

    /**
     * Set a custom app bar color to fit the bug report more your app style.
     *
     * @param color the background color of the app bar.
     */
    public static void setTintColor(String color) {
        FeedbackModel.getInstance().setAppBarColor(color);
    }

    /**
     * Attach cusom data, which can be view in the BugBattle dashboard.
     *
     * @param customData The data to attach to a bug report
     */
    public static void attachCustomData(JSONObject customData) {
        FeedbackModel.getInstance().setCustomData(customData);
    }

    /**
     * Set/Prefill the email address for the user.
     * @param email address, which is fileld in.
     */
    public static void setCustomerEmail(String email) {
        FeedbackModel.getInstance().setEmail(email);
    }

    /**
     * Sets the API url to your internal Bugbattle server. Please make sure that the server is reachable within the network.
     * @param apiUrl url of the internal Bugbattle server
     */
    public static void setApiURL(String apiUrl) {
        FeedbackModel.getInstance().setApiUrl(apiUrl);
    }
}
