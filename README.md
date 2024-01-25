
# DsvToJsonlConverter

Converts Delimiter Separated Values (DSV) to JSON Lines (JSONL) format.

## Overview

DsvToJsonlConverter is a Java-based tool that allows you to convert DSV files (CSV or other delimited formats) to JSON Lines format. This tool is designed to simplify the process of converting tabular data to a more flexible JSON format.

## How to Use

### Prerequisites

- Java 8 or higher installed on your machine.

### Usage

To use the DsvToJsonlConverter, follow these steps:

1. **Compile the Program (if needed):**

   If you haven't compiled the program yet, you can compile it using:

   ```bash
   javac DsvToJsonlConverter.java
   ```

2. **Run the Program:**

   Execute the following command to convert a DSV file to JSON Lines:

   ```bash
   java -jar DsvToJsonlConverter.jar input_file.csv output_file.jsonl ,
   ```

    - `input_file.csv`: The path to your input DSV file.
    - `output_file.jsonl`: The desired output JSON Lines file.
    - `,` (optional): The delimiter used in the DSV file. If not provided, the program will attempt to detect it.

### Example

```bash
java -jar DsvToJsonlConverter.jar sample.csv output.jsonl ;
```

## Dependencies

This project relies on Java 8 or higher.

## Contact

For questions or feedback, please contact Gollakaram Vishnu at vishnusrivardhangollakaram@gmail.com.

## Acknowledgments

- Inspired by [Project/Person]
- Used [Library/Tool] for [Purpose].
```

Replace `[Project/Person]`, `[Library/Tool]`, and other placeholders with appropriate information.