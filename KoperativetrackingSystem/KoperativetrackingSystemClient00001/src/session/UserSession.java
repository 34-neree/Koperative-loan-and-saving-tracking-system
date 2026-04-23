package session;

import model.Member;

public class UserSession {

    private static Member loggedInMember;
    private static String memberId;

    public static void login(Member member) {
        loggedInMember = member;
    }

    public static void login(String id) {
        memberId = id;
    }

    public static Member getLoggedInMember() {
        return loggedInMember;
    }

    public static String getMemberId() {
        if (memberId != null) return memberId;
        if (loggedInMember != null) return loggedInMember.getMemberId();
        return null;
    }

    public static void setMemberId(String id) {
        memberId = id;
    }

    public static void logout() {
        loggedInMember = null;
        memberId = null;
    }
}
