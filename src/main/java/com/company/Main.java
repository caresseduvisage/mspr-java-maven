package com.company;

import java.awt.Desktop;
import java.io.*;
import java.net.*;
import java.util.List;

import htmlGen.*;

public class Main {

    public static void main(String[] args) throws IOException {
        PageGen page = new PageGen();
        page.generateHomePage();
        page.generateAllAgentsPage();
        page.openTheWebsite();
    }

}

