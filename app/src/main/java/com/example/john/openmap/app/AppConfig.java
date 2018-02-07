package com.example.john.openmap.app;

public class AppConfig {
    // Server user login url
    private static String IP_ADDRESS = "192.168.35.14";

    public static String URL_LOGIN = "http://"+IP_ADDRESS+"/android_login_api/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://"+IP_ADDRESS+"/android_login_api/register.php";

    public static String CHECK_ID = "http://"+IP_ADDRESS+"/android_login_api/check_id.php";

    public static String NEW_LOCATION = "http://"+IP_ADDRESS+"/android_login_api/make_id.php";

    public static String UPLOAD_STORY = "http://"+IP_ADDRESS+"/android_login_api/uploaded_story_from_client.php";

    public static String UPLOAD_IMAGE = "http://"+IP_ADDRESS+"/android_login_api/upload_image_to_providers_area.php";

    public static String PROVIDER_MAIN = "http://"+IP_ADDRESS+"/android_login_api/main_page.php";

    public static String USERS_LOCATION = "http://"+IP_ADDRESS+"/android_login_api/users_location.php";

    public static String GET_PROVIDER_INFORMATION = "http://"+IP_ADDRESS+"/android_login_api/get_providers_head.php";

    public static String SEND_PROVIDERS_INFORMATION = "http://"+IP_ADDRESS+"/android_login_api/send_Provider_Head_Information.php";

    public static String ADRESS = "http://"+IP_ADDRESS+"/android_login_api/";
}
