<html>
<body>

	<div align="center">

		<h2>Lom Link Checker Web Service</h2>
		<p>

			<a href="webresources/linkchecker/listRepos">List Available
				Repositories</a><br> <a href="webresources/linkchecker/checkRepo">Check
				a repository</a>(example:checkRepo?repo=a_repo_name) <br> <a
				href="webresources/linkchecker/checkLink">Check a link</a>(example:checkLink?link=a_url_to_somewhere)<br>
			<a href="webresources/linkchecker/getResults">Get the Linkcheck
				results for a repository</a> (example:getResults?repo=a_repo_name)<br>
		<form action="uploadFile.jsp" method="post"
			enctype="multipart/form-data">
			<table>
				<tr>
					<td><label for="file">File to link check(only zip format):</label> <input type="file"
						name="file" id="file"></td>

					<td><input type="submit" name="submit" value="Submit"></td>
				</tr>


			</table>
		</form>

	</div>
</body>
</html>
