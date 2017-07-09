//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.18
//
package com.amazonaws.mobile;

import com.amazonaws.mobilehelper.config.AWSMobileHelperConfiguration;
import com.amazonaws.regions.Regions;

/**
 * This class defines constants for the developer's resource
 * identifiers and API keys. This configuration should not
 * be shared or posted to any public source code repository.
 */
public class AWSConfiguration {
    // AWS MobileHub user agent string
    public static final String AWS_MOBILEHUB_USER_AGENT =
        "MobileHub 86859f08-46b5-4566-b636-adeb18700bad aws-my-sample-app-android-v0.18";
    // AMAZON COGNITO
    public static final Regions AMAZON_COGNITO_REGION =
      Regions.fromName("us-east-1");
    public static final String  AMAZON_COGNITO_IDENTITY_POOL_ID =
        "us-east-1:591805f0-4075-46c3-b0a7-37e5348474aa";
    public static final String AMAZON_COGNITO_USER_POOL_ID =
        "us-east-1_6pWjEXH6X";
    public static final String AMAZON_COGNITO_USER_POOL_CLIENT_ID =
        "25fdhq6a3u6ua3qi2nssfs3jv8";
    public static final String AMAZON_COGNITO_USER_POOL_CLIENT_SECRET =
        "aeqnu8ulsd3i6jsop8c6mg0p82lja3ilhuu525rbklvtp4advo";

    private static final AWSMobileHelperConfiguration helperConfiguration = new AWSMobileHelperConfiguration.Builder()
        .withCognitoRegion(AMAZON_COGNITO_REGION)
        .withCognitoIdentityPoolId(AMAZON_COGNITO_IDENTITY_POOL_ID)
        .withCognitoUserPool(AMAZON_COGNITO_USER_POOL_ID,
            AMAZON_COGNITO_USER_POOL_CLIENT_ID, AMAZON_COGNITO_USER_POOL_CLIENT_SECRET)
        .build();
    /**
     * @return the configuration for AWSKit.
     */
    public static AWSMobileHelperConfiguration getAWSMobileHelperConfiguration() {
        return helperConfiguration;
    }
}
