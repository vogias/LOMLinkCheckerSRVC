package com.grnet.gr.LinkCheckerSRVC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/linkchecker")
public class MyResource {

	@Context
	private ServletContext context;

	private Properties getProps() throws IOException {
		String filename = "configure.properties";
		Properties props = new Properties();
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();

		InputStream isr = classLoader.getResourceAsStream(filename);

		if (isr != null) {
			InputStreamReader isrProperties = new InputStreamReader(isr);

			props.load(isrProperties);

		}

		return props;

	}

	@GET
	@Path("/checkRepo")
	@Produces(MediaType.APPLICATION_JSON)
	public Report checkRepo(
			@DefaultValue("COSMOS") @QueryParam("repo") String repo)
			throws IOException {

		lom_linkchecker linkchecker = new lom_linkchecker(this.getProps());

		linkchecker.checkLink(getProps().getProperty(Constants.folderPath)
				+ File.separator + repo,
				getProps().getProperty(Constants.badfolderPath));

		Report report = linkchecker.getReport();

		File resFolder = new File(context.getRealPath("") + "/results");
		resFolder.mkdir();

		File res = new File(resFolder, repo + "_LinkCheck_Results.txt");

		FileWriter fw = new FileWriter(res.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(report.toString());
		bw.close();

		return report;
	}

	@GET
	@Path("/getDReport")
	@Produces(MediaType.APPLICATION_JSON)
	public DReport getDReport(
			@DefaultValue("COSMOS") @QueryParam("repo") String repo)
			throws IOException {

		lom_linkchecker linkchecker = new lom_linkchecker(this.getProps());

		linkchecker.checkLink(getProps().getProperty(Constants.folderPath)
				+ File.separator + repo,
				getProps().getProperty(Constants.badfolderPath));

		// Report report = linkchecker.getReport();
		DReport dReport = linkchecker.getDReport();

		File resFolder = new File(context.getRealPath("") + "/results");
		resFolder.mkdir();

		File res = new File(resFolder, repo + "_LinkCheck_Results.txt");

		FileWriter fw = new FileWriter(res.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(dReport.toString());
		bw.close();

		return dReport;
	}

	@GET
	@Path("/checkLink")
	@Produces(MediaType.TEXT_PLAIN)
	public String checkLink(@QueryParam("link") String link) throws IOException {

		try {
			if (link.equals(""))
				return "No Link provided.";
		} catch (NullPointerException ex) {
			return "No Link provided.";
		}

		lom_linkchecker linkchecker = new lom_linkchecker(this.getProps());

		int result = linkchecker.URLChecker(link);

		return "Response code:" + result;
	}

	@GET
	@Path("/listRepos")
	@Produces(MediaType.APPLICATION_JSON)
	public Repos list() throws FileNotFoundException, IOException {

		Repos repos = new Repos();

		File frep = new File(getProps().getProperty(Constants.folderPath));

		File[] listFiles = frep.listFiles();

		for (int i = 0; i < listFiles.length; i++) {
			repos.addRepositories(listFiles[i].getName());
		}
		return repos;

	}

	@GET
	@Path("/getResults")
	@Produces(MediaType.TEXT_PLAIN)
	public String getResults(@QueryParam("repo") String repo)
			throws FileNotFoundException {

		if (repo == null)
			return "";
		else {
			File resFolder = new File(context.getRealPath("") + "/results");

			File[] listFiles = resFolder.listFiles();

			for (File file : listFiles) {

				if (file.getName().contains(repo)) {
					StringBuffer buffer = new StringBuffer();
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line;
					try {
						while ((line = br.readLine()) != null) {
							buffer.append(line + "\n");
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return "";

					} finally {
						try {
							if (br != null) {
								br.close();
								return buffer.toString();
							}

						} catch (IOException ex) {
							ex.printStackTrace();
							return "";
						}
					}

				} else
					continue;
			}

			return "";
		}

	}
}
