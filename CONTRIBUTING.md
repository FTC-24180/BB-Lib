## Who can contribute?
At the moment only our organization members can contribute to BB-Lib, however, everyone is still welcome to use and fork this repository. 

## Contributing
If you are a member of FTC-24180 then there are a few guidelines you need to know before contributing.
### Branch Information
- `master` is the only long lived branch and is pull request protected. Changes must always start in a feature branch.
-  When creating feature branches, they should be named in the following format: `username-feature` where username is your username and feature is the feature you are working on. If there are multiple words in the feature name use `_` to separate them. Ex: `garply65-update_docs`
- Once the feature is done create a pull request.
- If you are confident in your code you may merge your pull request without review. Only completed work up to date with master should be merged.
- After merging a feature branch delete it from remote. Make sure the feature branch is in sync with `master` before deleting it.
### Publishing a Version
- To publish a version to Jitpack create a release containing a commit on the master branch and tag it with the appropriate version number.
### Version Numbering
- **NEVER REUSE VERSION NUMBERS**
- Version numbers use a three number format that increments up by one based on the following criteria:
  - First number indicates breaking change
  - Second number indicates added feature
  - Third number indicates bug fix
- Ex: `1.3.23` = Breaking change 1, feature release 3, bug fix 23.
### Naming Conventions
We use standard Java naming conventions. This document will provide an overveiw for those unfamiliar with them. 
- Variables should be in camel case ex:`fooBar`
  - `boolean` should be a true or false statement ex:`isRobotMoving`
  - `int`/`double`/`string` should be a noun ex:`robotAngle` or `robotName`
- Constants should be nouns in screaming snake case ex:`ROBOT_WEIGHT`
- Functions should be verbs in camel case ex:`moveArm`
- Classes should be nouns in pascal case ex:`RobotArm`
- Files should be named in the same way as classes
- Special cases:
  - When there is a private member of a class that is set by a property in the class's constructor, instead of giving them different names use the `this.` syntax to set the private member in the constructor.
### Comments
- All classes, functions, and variables exposed to the consuming projects should have java doc comments that specify their functionality, usage, returns, and parameters.
- Use inline and block comments to denote file organization and specify unclear or important information for other contributors.
### Unit Tests
- Implement unit tests for all exposed functionality unless it is overly simple.
- Any number inputs (int or double) should test for 0.
### Common Abbreviations
This is a list of common abbreviations used in the code base.
- pos = position
- vel = velocity
- accel = acceleration
- ref = reference
