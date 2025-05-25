Used API parameters:

| Parameter                    | Meaning                                                                                                      |
|------------------------------|--------------------------------------------------------------------------------------------------------------|
| `offset=X`                   | X is a Unix timestamp or token string                                                                        |
| `descending=1`               | Fetch tenderRecords starting from `offset` **backward**                                                            |
| `descending=0`               | Fetch tenderRecords from `offset` **forward**                                                                      |
| `limit=100`                  | Response brings back 100 tenderRecords (default)                                                                   |
| `opt_fields=procuringEntity` | `opt_fields` List of additional fields to be included in the response. `procuringEntity` contact information |

## 🔍 Contact Extraction Strategy

The MVP goal of this project is to **extract contact information for each tenderRecord**, specifically from the `procuringEntity` field.

The Prozorro public API provides two main strategies for accessing this information:

---

### 🟢 Option 1: Use 
`opt_fields=procuringEntity`

**Query example:**

<pre>GET /api/2.4/tenderRecords?descending=1&limit=100&opt_fields=procuringEntity</pre>

This option requests only specific fields (e.g., `procuringEntity`) directly in the list response. It returns minimal data per tenderRecord, significantly reducing API load and request count.

**✅ Pros:**

- Only 1 HTTP request per page (100 tenderRecords)
- Fast and scalable
- Efficient for large-scale imports

**🔴 Cons:**

- `procuringEntity` may be incomplete or outdated compared to the full tenderRecord detail

---

### ❌ Option 2: Fetch Full Tender Details Per ID

**Query flow:**

<pre>GET /api/2.4/tenderRecords?descending=1&limit=100
GET /api/2.4/tenderRecords/{id}  // Repeated for each tenderRecord</pre>

This approach first retrieves a list of tenderRecord IDs, then makes a separate request for the full details of each tenderRecord.

**✅ Pros:**

- Complete and up-to-date tenderRecord data
- Access to all fields (e.g., documents, full descriptions, etc.)

**🔴 Cons:**

- 100x more HTTP requests per batch
- Slower and more error-prone
- Likely to hit rate limits or timeouts at scale

---

## ⚖️ Algorithm Complexity Comparison

| Strategy                     | HTTP Requests per 100 Tenders | Time Complexity per Batch | Notes                      |
|------------------------------|-------------------------------|--------------------------|----------------------------|
| `opt_fields=procuringEntity` | 1                             | **O(1)**                 | Best for performance       |
| Full tenderRecord fetch per ID     | 101                           | **O(n)**                 | Best for data completeness |

**Recommendation:** Use `opt_fields` for MVP, switch to full fetches only for tenderRecords where contact data appears incomplete.