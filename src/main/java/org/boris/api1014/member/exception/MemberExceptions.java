package org.boris.api1014.member.exception;

public enum MemberExceptions {
    BAD_AUTH(400, "ID/PW incorrect"),
    TOKEN_NOT_ENOUGH(401, "More Tokens required"),
    ACCESSTOKEN_TOO_SHORT(400,"Access Token too short"),
    REQUIRE_SIGN_IN(401, "Require sign in");


    private MemberTaskException exception;

    MemberExceptions(int status, String msg) {
        exception = new MemberTaskException(status, msg);
    }

    public MemberTaskException get() {
        return exception;
    }


}
