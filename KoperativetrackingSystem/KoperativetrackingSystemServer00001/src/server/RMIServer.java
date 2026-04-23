package server;

import rmi.MemberService;
import rmi.SavingsService;
import rmi.LoanService;
import rmi.ShareService;
import rmi.LoanRepaymentService;
import rmi.MeetingService;
import rmi.FineService;
import rmi.TransactionService;
import rmi.NotificationService;

import rmi.impl.MemberServiceImpl;
import rmi.impl.SavingsServiceImpl;
import rmi.impl.LoanServiceImpl;
import rmi.impl.ShareServiceImpl;
import rmi.impl.LoanRepaymentServiceImpl;
import rmi.impl.MeetingServiceImpl;
import rmi.impl.FineServiceImpl;
import rmi.impl.TransactionServiceImpl;
import rmi.impl.NotificationServiceImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {

    public static void main(String[] args) {

        try {
            int port = 3500; // must be between 3000–4000

            // Start RMI Registry
            Registry registry = LocateRegistry.createRegistry(port);

            // Create service instances — original modules
            MemberService memberService = new MemberServiceImpl();
            SavingsService savingsService = new SavingsServiceImpl();
            LoanService loanService = new LoanServiceImpl();

            // Create service instances — new modules
            ShareService shareService = new ShareServiceImpl();
            LoanRepaymentService loanRepaymentService = new LoanRepaymentServiceImpl();
            MeetingService meetingService = new MeetingServiceImpl();
            FineService fineService = new FineServiceImpl();
            TransactionService transactionService = new TransactionServiceImpl();
            NotificationService notificationService = new NotificationServiceImpl();

            // Bind original services
            registry.rebind("MemberService", memberService);
            registry.rebind("SavingsService", savingsService);
            registry.rebind("LoanService", loanService);

            // Bind new services
            registry.rebind("ShareService", shareService);
            registry.rebind("LoanRepaymentService", loanRepaymentService);
            registry.rebind("MeetingService", meetingService);
            registry.rebind("FineService", fineService);
            registry.rebind("TransactionService", transactionService);
            registry.rebind("NotificationService", notificationService);

            System.out.println("✅ RMI Server started on port " + port);
            System.out.println("✅ 9 services bound successfully:");
            System.out.println("   - MemberService");
            System.out.println("   - SavingsService");
            System.out.println("   - LoanService");
            System.out.println("   - ShareService");
            System.out.println("   - LoanRepaymentService");
            System.out.println("   - MeetingService");
            System.out.println("   - FineService");
            System.out.println("   - TransactionService");
            System.out.println("   - NotificationService");

        } catch (Exception e) {
            System.err.println("❌ RMI Server failed to start");
            e.printStackTrace();
        }
    }
}
