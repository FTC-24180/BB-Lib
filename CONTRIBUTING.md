## Who can contribute?
At the moment only our organazation members can contribute to BB-Lib, however, you are still welcome to use and fork this repository. 

## Contributing
If you are a member of FTC-24180 then there are a few guidelines you need to know before contributing.
### Branch Information
- `master` is the only long lived branch and is pull request protected. Changes must always start in a feature branch.
-  When creating feature branches, they should be named in the following format: `username-feature` where username is your username and feature is the feature you are working on.
- Once the feature is done create a pull request.
- If you are confident in your code you may merge your pull request without review. Only completed work up to date with master should be merged.
- After merging a feature branch delete it from remote. Make sure the feature branch is in sync with `master` before deleting it.
### Publishing a Version
- To publish a version to Jitpack create a release containing a commit on the master branch and tag it with the apropriate version number.
### Version Numbering
- **NEVER REUSE VERSION NUMBERS**
- Version numbers use a three number format that incriments up by one based on the following criteria:
  - First number indecates breaking change
  - Secound number indecates added feature
  - Third number indecates bug fix
- Ex: `1.3.23` = Breaking change 1, feature release 3, bugfix 23.
### Naming Conventions
We use standard Java naming conventions. This document will provide an overveiw for those unfamiliar with them. 
- Variables should be in camel case ex:`fooBar`
  - `boolean` should be a true or false statement ex:`isRobotMoving`
  - `int`/`double`/`string` should be a noun ex:`robotAngle` or `robotName`
- Constants should be nouns in screaming snake case ex:`ROBOT_WEIGHT`
- Functions should be verbs in camel case ex:`moveArm`
- Classes should be nouns in pascal case ex:`RobotArm`
- Files should be named in the same way as classes
- Special casses
  - When there is a private member of a class that is set by a property in the classe's constructor, instead of giving them different names use the `this.` syntax to set the private member in the constructor.
### Common Abbreviations
This is a list of common abbriviations used in the code base.
- pos = position
- vel = velocity
- accel = acceleration
- ref = reference
