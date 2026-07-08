## Who can contribute?
At the moment only our organazation members can contribute to BB-Lib, however, you are still welcome to use and fork this repository. 
This may change in the future but for now if you are not a member of FTC-24180 please adhere to the following:
- Do not ask to be a contributer for BB-Lib.
- Do not submit pull requests to BB-Lib.
- Do not open issues in BB-Lib.

## Contributing
If you are a member of FTC-24180 then there are a few guidelines you need to know before contributing.
### Branch Rules
1. `master` is the only long-lived branch. There is no `release`/`dev` split.
2. A new package version is published by pushing a Git tag (e.g. `git tag 0.3.0 && git push origin 0.3.0`),
not by pushing to a protected branch. JitPack builds it lazily the first time someone resolves that tag.
There is no version number to edit in `BB-Lib/build.gradle` - it reads the version from the tag itself.
See [CLAUDE.md](CLAUDE.md) for the full release process.
3. When creating feature branches, they should be named in the following format: `username-feature` where username is your username and feature is the feature you are working on.
4. When finished with your feature branch, merge it into `master` and delete it from remote. Make sure the feature branch is in sync with `master` before deleting it.
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
