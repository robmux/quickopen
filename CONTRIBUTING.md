# Contributing to GoLand Open Repo Plugin

We welcome contributions from the community! Here are some guidelines to help you get started:

## Adding Support for New Languages and Frameworks

1. **Identify the Project File**: Determine the main project file (like `go.mod` for Go projects) that contains the repository URL.
2. **Create a New Action**: Create a new action class to handle the extraction and opening of the repository URL and pull requests.
3. **Update `plugin.xml`**: Add your new action to the `plugin.xml` file.
4. **Refactor Code**: If necessary, refactor the shared code into utility classes to avoid duplication.

## Code Style

- Follow the existing coding style.
- Write clear and concise commit messages.

## Pull Request Process

1. Fork the repository.
2. Create a new branch for your feature or bugfix.
3. Make your changes and commit them.
4. Push your changes to your fork.
5. Submit a pull request to the `main` branch.

Thank you for contributing!
