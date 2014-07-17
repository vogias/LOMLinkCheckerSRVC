<%@page import="org.apache.commons.io.FileUtils"%>
<%@page import="java.net.URI"%>
<%@page import="javax.ws.rs.core.MultivaluedMap"%>
<%@page import="javax.ws.rs.core.MediaType"%>
<%@page import="com.sun.jersey.api.client.WebResource"%>
<%@page import="com.sun.jersey.api.client.Client"%>
<%@page import="com.sun.jersey.api.client.config.DefaultClientConfig"%>
<%@page import="com.sun.jersey.api.client.config.ClientConfig"%>
<%@page import="javax.ws.rs.core.UriBuilder"%>
<%@page import="java.util.zip.ZipInputStream"%>
<%@page import="java.util.zip.ZipEntry"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="org.apache.commons.fileupload.disk.*"%>
<%@ page import="org.apache.commons.fileupload.servlet.*"%>
<%@ page import="org.apache.commons.io.output.*"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItem"%>

<%
	File file;
	int maxFileSize = 5000 * 1024;
	int maxMemSize = 2000 * 1024;
	ServletContext context = pageContext.getServletContext();

	String filePath = context.getInitParameter("file-upload");
	String temp = context.getInitParameter("file-temp");

	// Verify the content type
	String contentType = request.getContentType();

	if ((contentType.indexOf("multipart/form-data") >= 0)) {

		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File(temp));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// maximum file size to be uploaded.
		upload.setSizeMax(maxFileSize);
		try {
			// Parse the request to get file items.
			List fileItems = upload.parseRequest(request);

			// Process the uploaded file items
			Iterator i = fileItems.iterator();

			out.println("<html>");
			out.println("<head>");
			out.println("<title>Link Checker Live check.</title>");
			out.println("</head>");
			out.println("<body>");
			

			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				
			
				if (!fi.isFormField()) {
					// Get the uploaded file parameters
					String fieldName = fi.getFieldName();
					String fileName = fi.getName();
					boolean isInMemory = fi.isInMemory();
					long sizeInBytes = fi.getSize();

					String fType = fileName.substring(
							fileName.lastIndexOf(".") + 1,
							fileName.length());
					if (fType.equals("zip")) {

						// Write the file
						if (fileName.lastIndexOf("\\") >= 0) {
							file = new File(filePath
									+ fileName.substring(fileName
											.lastIndexOf("\\")));
						} else {
							file = new File(filePath
									+ fileName.substring(fileName
											.lastIndexOf("\\") + 1));
						}
						fi.write(file);

						byte[] buffer = new byte[1024];
						try {

							File folder = new File(filePath,
									fileName.substring(0,
											fileName.lastIndexOf(".")));
							if (!folder.exists()) {
								folder.mkdir();
								System.out.println(folder
										.getAbsolutePath()
										+ " folder is created.");
							}

							//get the zip file content
							ZipInputStream zis = new ZipInputStream(
									new FileInputStream(
											file.getAbsolutePath()));

							//get the zipped file list entry
							ZipEntry ze = zis.getNextEntry();

							while (ze != null) {

								if (!ze.isDirectory()) {

									String zfName = ze.getName();
									zfName = zfName.substring(
											zfName.indexOf("/") + 1,
											zfName.length());

									String type = zfName
											.substring(
													zfName.lastIndexOf(".") + 1,
													zfName.length());
									if (type.equals("xml")) {
										File newFile = new File(folder,
												zfName);

										//create all non exists folders
										//else you will hit FileNotFoundException for compressed folder
										new File(newFile.getParent())
												.mkdirs();

										FileOutputStream fos = new FileOutputStream(
												newFile);

										int len;
										while ((len = zis.read(buffer)) > 0) {
											fos.write(buffer, 0, len);
										}

										fos.close();
									}

								}
								ze = zis.getNextEntry();

							}
							zis.closeEntry();
							zis.close();

							System.out.println("Done");

							file.delete();

							URI uri = UriBuilder
									.fromUri(
											"http://localhost:8080/LinkCheckerSRVC")
									.build();

							Client client = Client
									.create(new DefaultClientConfig());

							WebResource service = client.resource(uri);

							out.println(service
									.path("webresources")
									.path("linkchecker")
									.path("checkRepo")
									.queryParam("repo",
											folder.getName())
									.accept(MediaType.APPLICATION_JSON)
									.get(String.class)
									+ "<br>");
							FileUtils.deleteDirectory(folder);
						} catch (IOException ex) {
							ex.printStackTrace();

						}

					} else {

						out.println("<html>");
						out.println("<head>");
						out.println("<title>File upload</title>");
						out.println("</head>");
						out.println("<body>");
						out.println("<p>File must be of zip type</p>");
						out.println("</body>");
						out.println("</html>");

					}

				}
			}
			out.println("</body>");
			out.println("</html>");
		} catch (Exception ex) {
			System.out.println(ex);
		}
	} else {
		out.println("<html>");
		out.println("<head>");
		out.println("<title>File upload</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<p>No file uploaded</p>");
		out.println("</body>");
		out.println("</html>");
	}
%>