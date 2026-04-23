package client.rmi;

import rmi.MemberService;
import rmi.SavingsService;
import rmi.LoanService;
import rmi.ShareService;
import rmi.LoanRepaymentService;
import rmi.MeetingService;
import rmi.FineService;
import rmi.TransactionService;
import rmi.NotificationService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Central RMI Client Connector
 * Responsible for connecting to RMI Server and exposing all services
 */
public class RMIClient {

    private static final String HOST = "localhost";
    private static final int PORT = 3500;

    // Original services
    private static MemberService memberService;
    private static SavingsService savingsService;
    private static LoanService loanService;

    // New services
    private static ShareService shareService;
    private static LoanRepaymentService loanRepaymentService;
    private static MeetingService meetingService;
    private static FineService fineService;
    private static TransactionService transactionService;
    private static NotificationService notificationService;

    // Static block runs once when class is loaded
    static {
        try {
            Registry registry = LocateRegistry.getRegistry(HOST, PORT);

            // Original service lookups
            memberService = (MemberService) registry.lookup("MemberService");
            savingsService = (SavingsService) registry.lookup("SavingsService");
            loanService = (LoanService) registry.lookup("LoanService");

            // New service lookups
            shareService = (ShareService) registry.lookup("ShareService");
            loanRepaymentService = (LoanRepaymentService) registry.lookup("LoanRepaymentService");
            meetingService = (MeetingService) registry.lookup("MeetingService");
            fineService = (FineService) registry.lookup("FineService");
            transactionService = (TransactionService) registry.lookup("TransactionService");
            notificationService = (NotificationService) registry.lookup("NotificationService");

            System.out.println("✅ Connected to RMI Server at " + HOST + ":" + PORT);
            System.out.println("✅ 9 services connected");

        } catch (Exception e) {
            System.err.println("❌ Failed to connect to RMI Server");
            e.printStackTrace();
        }
    }

    // Original getters
    public static MemberService getMemberService() { return memberService; }
    public static SavingsService getSavingsService() { return savingsService; }
    public static LoanService getLoanService() { return loanService; }

    // New getters
    public static ShareService getShareService() { return shareService; }
    public static LoanRepaymentService getLoanRepaymentService() { return loanRepaymentService; }
    public static MeetingService getMeetingService() { return meetingService; }
    public static FineService getFineService() { return fineService; }
    public static TransactionService getTransactionService() { return transactionService; }
    public static NotificationService getNotificationService() { return notificationService; }
}
