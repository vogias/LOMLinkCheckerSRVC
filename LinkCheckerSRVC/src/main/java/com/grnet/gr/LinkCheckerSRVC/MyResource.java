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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("/linkchecker")
@Api(value = "/linkchecker", description = "LOM/DC based Link Checker")
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
	@ApiOperation(value = "Link Check a specific repository", notes = "In order to check a repository list first all available repositories and choose one.", response = Report.class)
	@Produces(MediaType.APPLICATION_JSON)
	public Report checkRepo(
			@ApiParam(value = "Repository Name", required = true) @QueryParam("repo") String repo)
			throws IOException {

		lom_linkchecker linkchecker = new lom_linkchecker(this.getProps());

		linkchecker.checkLink(getProps().getProperty(Constants.folderPath)
				+ File.separator + repo,
				getProps().getProperty(Constants.badfolderPath));

		Report report = linkchecker.getReport();
		DReport dReport = linkchecker.getDReport();

		File resFolder = new File(context.getRealPath("") + "/results");
		resFolder.mkdir();

		File res = new File(resFolder, repo + "_LinkCheck_Results.txt");
		File Dres = new File(resFolder, repo + "_LinkCheck_DetailedResults.txt");

		FileWriter fw = new FileWriter(res.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("Date", report.getDate());
			jsonObject.put("Repository", report.getRepo());
			jsonObject.put("Duration", report.getDuration());
			jsonObject.put("NumOfRecords", report.getNumberofcheckedrecords());
			jsonObject.put("LiveLinks", report.getHealthy());
			jsonObject.put("DeadLinks", report.getBrokenlinks());
			jsonObject.put("NWFLinks", report.getNotwellformed());

			bw.write(jsonObject.toString());
			bw.close();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FileWriter fdw = new FileWriter(Dres.getAbsoluteFile());
		BufferedWriter bdw = new BufferedWriter(fdw);

		JSONObject jsonObject2 = new JSONObject();
		try {
			jsonObject2.put("Date", dReport.getDate());
			jsonObject2.put("Repository", dReport.getRepository());
			jsonObject2.put("LiveLinks", dReport.getLive());
			jsonObject2.put("DeadLinks", dReport.getBroken());
			jsonObject2.put("NWFLinks", dReport.getNotWellFormed());

			bdw.write(jsonObject2.toString());
			bdw.close();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return report;
	}

	@GET
	@Path("/checkLink")
	@ApiOperation(value = "Link Check a given link", notes = "Insert the desired link.")
	@Produces(MediaType.TEXT_PLAIN)
	public String checkLink(
			@ApiParam(value = "URL", required = true) @QueryParam("link") String link)
			throws IOException {

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
	@ApiOperation(value = "List all available repositories", response = Repos.class)
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
	@ApiOperation(value = "Get the link checking results for a specific repository", notes = "In order to get the general results for a specific repository,list first all already repositories and choose one.", response = Report.class)
	@Produces(MediaType.TEXT_PLAIN)
	public String getResults(
			@ApiParam(value = "Repository", required = true) @QueryParam("repo") String repo)
			throws FileNotFoundException {

		if (repo == null)
			return "";
		else {
			File resFolder = new File(context.getRealPath("") + "/results");

			File[] listFiles = resFolder.listFiles();

			for (File file : listFiles) {

				if (file.getName().contains(repo + "_LinkCheck_Results")) {
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

	@GET
	@Path("/getDetailedResults")
	@ApiOperation(value = "Get the a detailed version of the link checking results for a specific analyzed repository", notes = "In order to get the detailed results for a specific analyzed repository,list first all already repositories and choose one.", response = DReport.class)
	@Produces(MediaType.TEXT_PLAIN)
	public String getDetailedResults(
			@ApiParam(value = "Repository Name", required = true) @QueryParam("repo") String repo)
			throws FileNotFoundException {

		if (repo == null)
			return "";
		else {
			File resFolder = new File(context.getRealPath("") + "/results");

			File[] listFiles = resFolder.listFiles();

			for (File file : listFiles) {

				if (file.getName()
						.contains(repo + "_LinkCheck_DetailedResults")) {
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
