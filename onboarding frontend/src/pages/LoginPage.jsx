import Login from "../components/Login";
import Navbar from "../components/Navbar";

export default function LoginPage({setLoading}){
    return(
        <>
        <Navbar />
        <Login setLoading={setLoading} />
        </>
    )
}