To increment the version, you need to create a new Git tag. This is a manual step and should be done whenever you want to release a new version.

	1.	Create a New Tag for Each Version Increment:
	For example, if you want to go from v0.9.0 to v1.0.0:

git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0

	•	The -a option specifies that it’s an annotated tag.
	•	-m allows you to provide a message for the tag.
	After this, the version should automatically reflect v1.0.0 when you build your project.

	2.	Incrementing Minor Versions:
	Similarly, if you want to increment a minor version (e.g., v0.9.1):

git tag -a v0.9.1 -m "Minor update"
git push origin v0.9.1

	3.	Versioning with SNAPSHOT or Pre-Release Labels:
	You can also use additional labels like -alpha, -beta, or -rc1 in your tags to represent pre-release versions:

git tag -a v1.1.0-beta1 -m "First beta of 1.1.0"
git push origin v1.1.0-beta1

	This would create a version like v1.1.0-beta1.
