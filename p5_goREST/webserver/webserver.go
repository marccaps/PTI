package main

import (
        "fmt"
        "log"
        "net/http"
        "github.com/gorilla/mux"
        "encoding/json"
        "encoding/csv"
    "io"
    "os"
    "io/ioutil"
    "strconv"
    "bufio"
    )

type RequestMessage struct {
    CarMaker string
    CarModel string
    NumDays string
    NumUnits string
}

type ResponseMessage struct {
    Field1 string
    Field2 string
}

func main() {

router := mux.NewRouter().StrictSlash(true)
router.HandleFunc("/", Index)
router.HandleFunc("/listCar", endpointFunc)
router.HandleFunc("/registerCar/{param}", endpointFunc2JSONInput)

log.Fatal(http.ListenAndServe(":8081", router))
}

func Index(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintln(w, "Service OK")
}

func endpointFunc(w http.ResponseWriter, r *http.Request) {
    readToFile(w)
}

func endpointFunc2JSONInput(w http.ResponseWriter, r *http.Request) {
    var requestMessage RequestMessage
    body, err := ioutil.ReadAll(io.LimitReader(r.Body, 1048576))
    if err != nil {
        panic(err)
    }
    if err := r.Body.Close(); err != nil {
        panic(err)
    }
    if err := json.Unmarshal(body, &requestMessage); err != nil {
        w.Header().Set("Content-Type", "application/json; charset=UTF-8")
        w.WriteHeader(422) // unprocessable entity
        if err := json.NewEncoder(w).Encode(err); err != nil {
            panic(err)
        }
    } else {
        fmt.Fprintln(w, "Successfully received car registration. Here is your rental:\n",requestMessage)
        writeToFile(w,requestMessage)
    }
}

func writeToFile(w http.ResponseWriter,r RequestMessage) {

    var precio int = 100
    numDays,err := strconv.Atoi(r.NumDays)
    numUnits,err := strconv.Atoi(r.NumUnits)
    precio = numUnits*numDays*precio
    fmt.Fprintln(w, "precio",precio)
    file, err := os.OpenFile("rentals.csv", os.O_APPEND|os.O_WRONLY|os.O_CREATE, 0600)
    if err!=nil {
        json.NewEncoder(w).Encode(err)
        return
    }
    writer := csv.NewWriter(file)

    var data1 = []string{r.CarMaker, r.CarModel,r.NumDays,r.NumUnits,strconv.Itoa(precio)}
    writer.Write(data1)
    writer.Flush()
    file.Close()
}

func readToFile(w http.ResponseWriter) {
    file, err := os.Open("rentals.csv")
    if err!=nil {
    json.NewEncoder(w).Encode(err)
    return
    }
    reader := csv.NewReader(bufio.NewReader(file))
    for {
        record, err := reader.Read()
        if err == io.EOF {
                break
            }
        fmt.Fprintln(w,"Lloguer:", record[0])
        fmt.Fprintln(w,"CarMaker:", record[0])
        fmt.Fprintln(w,"CarModel:", record[1])
        fmt.Fprintln(w,"NumDays:", record[2])
        fmt.Fprintln(w,"NumUnits:", record[3])
        fmt.Fprintln(w,"Precio:", record[4])
        fmt.Fprintln(w,"-------------------------------\n")

    }
  }
