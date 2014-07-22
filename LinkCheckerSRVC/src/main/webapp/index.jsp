<html>
<body>

	<div align="center">

		<h2>Lom Link Checker Web Service</h2>
		<p>

			<a href="webresources/linkchecker/listRepos">List Available
				Repositories</a> <a href="webresources/linkchecker/checkRepo">Check
				a repository(example:checkRepo?repo=COSMOS)</a> <br> <a
				href="webresources/linkchecker/checkLink">Check a
				link(example:checkLink?link=a_url_to_somewhere)</a><br>
				<a
				href="webresources/linkchecker/getResults">Get the Linkcheck results for a repository (example:getResults?repo=a_repo_name)</a><br>
				
		<form action="uploadFile.jsp" method="post"
			enctype="multipart/form-data">
			<table>
				<tr>
					<td><label for="file">Filename:</label> <input type="file"
						name="file" id="file"></td>

					<td><input type="submit" name="submit" value="Submit"></td>
				</tr>


			</table>
		</form>

	</div>
</body>
</html>
