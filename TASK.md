# Task: Parse Data from prozorro.gov.ua and Generate Excel Report
**Priority**: P1  
**Assignee**: [Dev/Team Name]

## **Description**
Extract procurement data from [prozorro.gov.ua](https://prozorro.gov.ua/) and generate an Excel report (`Report.xlsx`) with the following columns:
1. **Code EDRPOU** (Ukrainian company identifier)
2. **Customer Name**
3. **Customer Location**
4. **Customer Contacts**

## **Input/Output**
- **Input**: Web resource (prozorro.gov.ua).
- **Output**: Excel file (`Report.xlsx`) with the specified columns.

## **Technical Requirements**
1. **Data Extraction**:
    - Use **web scraping** (e.g., Jsoup, Selenium) or **Prozorro’s API** (if available).
    - Handle pagination (no entry limits; fetch all available data).
2. **Data Processing**:
    - Validate EDRPOU codes (ensure 8-10 digits).
    - Normalize customer locations (e.g., format as "City, Region").
3. **Output**:
    - Generate Excel file using **Apache POI** or **EasyExcel**.
    - Include error handling for missing fields.

## **Filters for Data Analysis**
Since no restrictions exist, clarify with stakeholders:
- **Timeframe**: Last 6 months vs. all historical data.
- **Procurement Status**: Only "completed" tenders?
- **Customer Type**: Government agencies only?
- **Region Filter**: Specific oblasts (e.g., Kyiv, Lviv)?

*(Await confirmation; proceed with no filters if unclarified.)*

## **Acceptance Criteria**
- [ ] Script extracts all available entries (no arbitrary limits).
- [ ] `Report.xlsx` contains all 4 columns with valid data.
- [ ] Empty fields are marked as "N/A".
- [ ] Documentation includes scraping methodology (API vs. HTML).

## **Risks & Mitigation**
- **Anti-Scraping Measures**: Use proxies/delays if blocked.
- **Data Volume**: Optimize memory (stream data if >100k rows).

## **References**
- [Prozorro API Documentation](https://prozorro.gov.ua/en/api) (if applicable)
- Sample EDRPOU validation regex: `^\d{8,10}$`  
