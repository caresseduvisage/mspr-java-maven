package htmlGen;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class PageGen {
    File home;

    public List<String> readList(String inputUrl) throws IOException {
        List<String> result = new ArrayList<String>();
        URL url = new URL(inputUrl);
        BufferedReader read = new BufferedReader(
                new InputStreamReader(url.openStream()));
        String i;
        while ((i = read.readLine()) != null)
            result.add(i);
        read.close();

        return result;
    }

    public void generateHomePage() throws IOException {
        System.out.println("Génération de la home page");
        // Déclare la variable contenant la liste des agents pour les afficher dans l'accueil
        List<String> agentList = readList("https://raw.githubusercontent.com/caresseduvisage/mspr-java/main/staff.txt");

        // On créer le fichier html
        File f = new File("public/index.html");

        // On initie le truc qui permet d'écrire dans ce fichier
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));

        // On écrit dedans avec cette methode
        bw.write("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x\" crossorigin=\"anonymous\">\n" +
                "    <link rel=\"stylesheet\" href=\"style.css\">\n" +
                "    <style>" +
                "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400&display=swap');" +
                "        * {" +
                "            font-family: Roboto;" +
                "        }" +
                "        .bg-th-green {" +
                "            background-color: #75a30a;" +
                "        }" +
                "        .bg-th-blue {" +
                "            background-color: #1faed1;" +
                "        }" +
                "    </style>" +
                "    <title>GO Securi</title>\n" +
                "</head>\n" +
                "<body class='bg-light'>\n" +
                "\n" +
                "<nav class='navbar navbar-expand-lg navbar-dark bg-th-blue'>"+
                "    <div class='container-fluid'>" +
                "     <a class='navbar-brand' href='#'>GO Securi</a>" +
                "            <ul class='navbar-nav me-auto mb-2 mb-lg-0'>" +
                "            <li class='nav-item'>" +
                "                <a class='nav-link' href='index.html'>Accueil</a>" +
                "            </li>" +
                "        </ul>" +
                "    </div>" +
                "</nav> "+
                "\n" +
                "    <div class=\"container mt-5\">\n" +
                "        <div class=\"text-center\">\n" +
                "            <h3>Choisir un agent</h3>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"container mt-5\">\n" +
                "        <div class=\"d-flex flex-wrap justify-content-center\">");

        // Avec cette boucle on génère le contenu dynamique récupéré depuis l'url github
        for (String agentName : agentList)
            bw.write("<div class='m-3'><a href='"+agentName+".html'>"+agentName+"</a></div>");

        bw.write("        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>");

        // On ferme l'écriture
        bw.close();

        // On enregistre le fichier généré en tant qu'attribut pour pouvoir le réouvrir plus tard
        this.home = f;
    }

    public void generateAllAgentsPage() throws IOException {
        List<String> agentList = readList("https://raw.githubusercontent.com/caresseduvisage/mspr-java/main/staff.txt");
        System.out.println("Génération des agents");
        for (String agentName : agentList)
            generateAgentPage(agentName);
    }

    public void generateAgentPage(String agent) throws IOException {
        List<String> agentData = readList("https://raw.githubusercontent.com/caresseduvisage/mspr-java/main/data/" + agent +".txt");
        String agentNom = agentData.get(0);
        String agentPrenom = agentData.get(1);
        String agentMetier = agentData.get(2);
        String agentPass = agentData.get(3);
        agentData.remove(0);
        agentData.remove(0);
        agentData.remove(0);
        agentData.remove(0);
        agentData.remove(0);

        System.out.println("Génération de la page agent - "+agent+"...");

        Map<String, String> matData = getAgentData(agentData);
        List<String> allMatList = getMaterialList();
        Map<String, String> matName = getMaterialName();

        File f = new File("public/" + agent + ".html");
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.write("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x\" crossorigin=\"anonymous\">\n" +
                "    <style>\n" +
                "        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400&display=swap');\n" +
                "        * {font-family: Roboto;}\n" +
                "        .bg-th-green {background-color: #75a30a;}\n" +
                "        .color-th {color: #75a30a;}\n" +
                "        .bg-th-blue {background-color: #1faed1;}\n" +
                "        td { width: 50%; text-align: center}\n" +
                "    </style>\n" +
                "    <title>GO Securi</title>\n" +
                "</head>\n" +
                "<body class=\"bg-light\">\n" +
                "    <nav class='navbar navbar-expand-lg navbar-dark bg-th-blue'>\n" +
                "        <div class='container-fluid'>\n" +
                "          <a class='navbar-brand' href='#'>GO Securi</a>\n" +
                "            <ul class='navbar-nav me-auto mb-2 mb-lg-0'>\n" +
                "                <li class='nav-item'>\n" +
                "                    <a class='nav-link' href='index.html'>Accueil</a>\n" +
                "                </li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "    </nav>\n" +
                "\n" +
                "    <div class=\"container mt-5 col-lg-5\"><a href=\"index.html\">Retour</a></div>\n" +
                "    <div class=\"container col-lg-5 p-3 shadow-sm bg-white rounded\">\n" +
                "        <div class=\"d-flex flex-wrap justify-content-between\">\n" +
                "            <div class=\"color-th\">\n" +
                "                <h3>" + agentNom + " " + agentPrenom + "</h3><br>\n" +
                "                 <div class='color-th-b'>" +
                "                   Fonction : " + agentMetier + " <br>" +
                "                   Mot de passe : " + agentPass + " <br>" +
                "                   </div>" +
                "            </div>\n" +
                "            <div>\n" +
                "                <img src='img/" + agent + ".jpg'>" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"container mt-5 col-lg-5 p-3 shadow-sm rounded bg-white\">\n" +
                "        <div class=\"d-flex flex-wrap justify-content-between\">\n" +
                "            <table class=\"table table-sm table-bordered\">\n" +
                "                <thead>\n" +
                "                  <tr>\n" +
                "                  </tr>\n" +
                "                </thead>\n" +
                "                <tbody>\n");

        for (String e : allMatList)
            bw.write("<tr><td>"+ matName.get(e) +"</td><td><input class='form-check-input' disabled type='checkbox' "+ matData.get(e) +"></td></tr>");

        bw.write("</tbody>\n" +
                "              </table>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>");

        bw.close();
    }

    public void openTheWebsite() throws IOException {
        Desktop.getDesktop().browse(this.home.toURI());
    }

    private Map<String, String> getAgentData(List<String> data) throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        List<String> matList = readList("https://raw.githubusercontent.com/caresseduvisage/mspr-java/main/liste.txt");
        List<String> allMaterial = new ArrayList<String>();

        // Récupère la liste complète du matériel
        for (String e : matList) {
            List<String> material = splitMaterial(e);
            map.put(material.get(0), "");
            allMaterial.add(material.get(1));
        }

        // Assigne le matériel que l'agent possède
        for (String elem : data)
            map.put(elem, "checked");

        return map;
    }

    private List<String> splitMaterial(String line) {
        List<String> m = Arrays.asList(line.split("\\t"));
        return m;
    }

    private List<String> getMaterialList() throws IOException {
        List<String> matList = readList("https://raw.githubusercontent.com/caresseduvisage/mspr-java/main/liste.txt");
        List<String> allMaterial = new ArrayList<String>();

        for (String e : matList) {
            List<String> material = splitMaterial(e);
            allMaterial.add(material.get(0));
        }
        return allMaterial;
    }

    private Map<String, String> getMaterialName() throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        List<String> matList = readList("https://raw.githubusercontent.com/caresseduvisage/mspr-java/main/liste.txt");

        for (String e : matList) {
            List<String> material = splitMaterial(e);
            map.put(material.get(0), material.get(1));
        }

        return map;
    }

}
