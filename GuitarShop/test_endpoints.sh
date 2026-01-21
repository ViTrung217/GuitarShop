#!/bin/bash

# Guitar Shop - API Testing Script
# Chạy các endpoint chính để kiểm tra lỗi

BASE_URL="http://localhost:8080"
RESULTS_FILE="test_results.txt"

echo "🎸 Guitar Shop - API Testing" > $RESULTS_FILE
echo "=============================" >> $RESULTS_FILE
echo "Timestamp: $(date)" >> $RESULTS_FILE
echo "" >> $RESULTS_FILE

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

test_endpoint() {
    local method=$1
    local endpoint=$2
    local description=$3
    
    echo -n "Testing $description... "
    
    if [ "$method" = "GET" ]; then
        response=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL$endpoint")
    else
        response=$(curl -s -o /dev/null -w "%{http_code}" -X $method "$BASE_URL$endpoint")
    fi
    
    if [ "$response" = "200" ] || [ "$response" = "301" ] || [ "$response" = "302" ]; then
        echo -e "${GREEN}✓${NC} ($response)"
        echo "✓ $description: $response" >> $RESULTS_FILE
    else
        echo -e "${RED}✗${NC} ($response)"
        echo "✗ $description: $response" >> $RESULTS_FILE
    fi
}

echo "Waiting for application to be ready..."
sleep 3

echo ""
echo "========== PUBLIC ROUTES =========="
test_endpoint "GET" "/" "Homepage"
test_endpoint "GET" "/login" "Login page"
test_endpoint "GET" "/register" "Register page"
test_endpoint "GET" "/cart" "Cart page"

echo ""
echo "========== PRODUCT ROUTES =========="
test_endpoint "GET" "/product/1" "Product detail (ID 1)"
test_endpoint "GET" "/product/2" "Product detail (ID 2)"

echo ""
echo "========== ADMIN ROUTES =========="
test_endpoint "GET" "/admin/dashboard" "Admin dashboard"
test_endpoint "GET" "/admin/products" "Admin products list"
test_endpoint "GET" "/admin/categories" "Admin categories list"
test_endpoint "GET" "/admin/users" "Admin users list"
test_endpoint "GET" "/admin/orders" "Admin orders list"

echo ""
echo "========== RESULTS =========="
echo ""
echo "Full results saved to: $RESULTS_FILE"
cat $RESULTS_FILE
