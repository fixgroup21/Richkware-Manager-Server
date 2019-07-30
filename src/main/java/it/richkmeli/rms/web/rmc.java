package it.richkmeli.rms.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.richkmeli.jframework.crypto.Crypto;
import it.richkmeli.jframework.database.DatabaseException;
import it.richkmeli.rms.data.device.DeviceDatabaseManager;
import it.richkmeli.rms.data.device.model.Device;
import it.richkmeli.rms.data.rmc.model.RMC;
import it.richkmeli.rms.web.response.KOResponse;
import it.richkmeli.rms.web.response.OKResponse;
import it.richkmeli.rms.web.response.StatusCode;
import it.richkmeli.rms.web.util.ServletManager;
import it.richkmeli.rms.web.util.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet({"/rmc"})
public class rmc extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        HttpSession httpSession = req.getSession();
        Session session = null;

        try {
//            BufferedReader br = req.getReader();
//            String request = br.readLine();

            session = ServletManager.getServerSession(httpSession);

            List<RMC> clients = null;

            if (session.isAdmin()) {
                //ottiene tutti i client presenti sul db
                System.out.println("Sono qui");
                clients = session.getRmcDatabaseManager().getRMCs();
                System.out.println("query fatta");
            } else {
                //ottiene tutti i client associati al suo account
                clients = session.getRmcDatabaseManager().getRMCs(session.getUser());
            }

            out.println(new OKResponse(StatusCode.SUCCESS, generateRmcListJSON(clients)).json());
            out.flush();
            out.close();
        } catch (it.richkmeli.rms.web.util.ServletException | DatabaseException e) {
            out.println(new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage()).json());
        }
    }


    private String generateRmcListJSON(List<RMC> clients) {
        Type type = new TypeToken<List<Device>>() {
        }.getType();
        Gson gson = new Gson();

        // oggetto -> gson
        String rmcListJSON = gson.toJson(clients, type);

        return rmcListJSON;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        super.doDelete(req, resp);
        PrintWriter out = resp.getWriter();
        HttpSession httpSession = req.getSession();
        Session session = null;

        // todo controlla che stia cancellando un rmc di cui ha i permessi

        // param crittati se rmc

        if (req.getParameterMap().containsKey("rmc")) {
            String rmc = req.getParameter("rmc");
            File secureDataServer = new File("TESTsecureDataServer.txt");
            String serverKey = "testkeyServer";
            Crypto.Server cryptoServer = new Crypto.Server();
            cryptoServer.init(secureDataServer, serverKey, rmc, "");
            cryptoServer.deleteClientData();
        }

        try {
            session = ServletManager.getServerSession(httpSession);

            // TODO cancella utente specifico, decidi se farlo solo da autenticato, magari con email o altro fattore di auth
            session.getCryptoServer().deleteClientData();


        } catch (it.richkmeli.rms.web.util.ServletException e) {
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
        }

    }
}
