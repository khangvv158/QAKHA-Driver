package com.example.qakhadriver.utils

object RegexKey {

    const val VALID_EMAIL_REGEX =
            "^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$"
    const val VALID_PHONE_REGEX =
            "(0[3|5|7|8|9])+([0-9]{8})\\b"
    const val VALID_PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}\$"
    const val VALID_ID_CARD_REGEX = "^[0-9]{9,}\$"
    const val VALID_LICENSE_PLATE_REGEX = "^[0-9]{2}[A-Z]{1}[0-9]{1}-[0-9]{5}\$"
}
