import { CircularProgress } from '@mui/material';

export default function Loading() {
    return (    
        <div className="flex justify-center items-center h-screen relative z-50">
            <CircularProgress />
        </div>
    )
}