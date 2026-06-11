# Exercise: User Profile and Order Customer Mappings

## What You'll Build

Add four annotations across four entities to wire up User ↔ UserProfile
(bidirectional @OneToOne with shared PK) and Order → Customer
(@ManyToOne with LAZY fetch).

## Requirements

- User has @OneToOne(mappedBy="user", cascade=ALL) UserProfile
- UserProfile has @OneToOne + @MapsId + @JoinColumn for the shared PK
- Order has @ManyToOne(fetch = FetchType.LAZY) Customer
- The pre-written RelationshipTest verifies cascade, shared PK,
  and N+1 behavior

## Starter Code

- PostgreSQL schema and seed data
- User, UserProfile, Customer, Order — all fields complete; only the
  relationship annotations have TODOs (4 spots total)
- OrderRepository has findAllWithCustomer JOIN FETCH already written
- RelationshipTest pre-written, currently fails
- SQL logging on

## Verification

- All RelationshipTest methods pass
- Cascade and shared-PK behavior visible in SQL logs
- JOIN FETCH path fires exactly 1 query; LAZY path fires 1+N

## Common Mistakes

- Forgetting @MapsId
- Two owning sides on a bidirectional @OneToOne
- fetch = EAGER as an N+1 "fix"
- Accessing LAZY outside a transaction